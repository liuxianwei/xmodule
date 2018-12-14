package com.penglecode.xmodule.common.web.shiro.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.util.Assert;

public class ClientUrlAccessFilterManager {

	private final Map<String,ClientUrlAccessFilter> clientUrlAccessFilters;
	
	private final ClientUrlAccessFilter defaultClientUrlAccessFilter;

	public ClientUrlAccessFilterManager(Map<String, ClientUrlAccessFilter> clientUrlAccessFilters) {
		super();
		Assert.notNull(clientUrlAccessFilters, "Property 'clientUrlAccessFilters' must be not null!");
		this.clientUrlAccessFilters = clientUrlAccessFilters;
		ClientUrlAccessFilter defaultClientUrlAccessFilter = null;
		for(Map.Entry<String,ClientUrlAccessFilter> entry : clientUrlAccessFilters.entrySet()) {
			if(asDefaultClientUrlAccessFilter(entry.getValue())) {
				defaultClientUrlAccessFilter = entry.getValue();
			}
		}
		if(defaultClientUrlAccessFilter == null) {
			defaultClientUrlAccessFilter = new AnonClientUrlAccessFilter();
		}
		this.defaultClientUrlAccessFilter = defaultClientUrlAccessFilter;
	}

	protected boolean asDefaultClientUrlAccessFilter(ClientUrlAccessFilter clientUrlAccessFilter) {
		if(clientUrlAccessFilter instanceof AnonClientUrlAccessFilter) {
			return true;
		}
		return false;
	}
	
	public List<ClientUrlAccessFilter> getMatchedClientUrlAccessFilters(HttpServletRequest request, HttpServletResponse response, String accessUrl) {
		List<ClientUrlAccessFilter> matchedUrlAccessHandlers = new ArrayList<ClientUrlAccessFilter>();
		for(Map.Entry<String,ClientUrlAccessFilter> entry : clientUrlAccessFilters.entrySet()) {
			ClientUrlAccessFilter urlAccessHandler = entry.getValue();
			if(urlAccessHandler.isMatchedFilter(request, response, accessUrl)) {
				matchedUrlAccessHandlers.add(urlAccessHandler);
			}
		}
		return matchedUrlAccessHandlers;
	}

	public void applyFilterChains(Map<String,String> chains) {
		if(!CollectionUtils.isEmpty(chains)) {
			Map<String,Map<String,Object>> chainConfigMapping = new LinkedHashMap<String,Map<String,Object>>();
			
			for (Map.Entry<String, String> entry : chains.entrySet()) {
	            String url = entry.getKey();
	            String chainDefinition = entry.getValue();
	            applyFilterChain(url, chainDefinition, chainConfigMapping);
	        }
			
			for(Map.Entry<String,Map<String,Object>> entry : chainConfigMapping.entrySet()) {
				ClientUrlAccessFilter clientUrlAccessFilter = getClientUrlAccessFilter(entry.getKey());
				clientUrlAccessFilter.setAppliedPaths(entry.getValue());
			}
		}
	}
	
	protected void applyFilterChain(String chainName, String chainDefinition, Map<String,Map<String,Object>> chainConfigMapping) {
		Assert.hasText(chainName, "chainName cannot be null or empty.");
		Assert.hasText(chainDefinition, "chainDefinition cannot be null or empty.");
		String[] filterTokens = splitChainDefinition(chainDefinition);
		for (String token : filterTokens) {
            String[] nameConfigPair = toNameConfigPair(token);

            String filterName = nameConfigPair[0];
            String chainConfig = nameConfigPair[1];
            
            String[] values = null;
            if (chainConfig != null) {
            	values = StringUtils.split(chainConfig);
            }
            Map<String,Object> appliedPaths = chainConfigMapping.get(filterName);
            if(appliedPaths == null) {
            	appliedPaths = new LinkedHashMap<String,Object>();
            	chainConfigMapping.put(filterName, appliedPaths);
            }
            appliedPaths.put(chainName, values);
        }
	}
	
	protected ClientUrlAccessFilter getClientUrlAccessFilter(String filterName) {
		return clientUrlAccessFilters.get(filterName);
	}
	
	protected String[] splitChainDefinition(String chainDefinition) {
        return StringUtils.split(chainDefinition, StringUtils.DEFAULT_DELIMITER_CHAR, '[', ']', true, true);
    }
	
	protected String[] toNameConfigPair(String token) throws ConfigurationException {

        try {
            String[] pair = token.split("\\[", 2);
            String name = StringUtils.clean(pair[0]);

            if (name == null) {
                throw new IllegalArgumentException("Filter name not found for filter chain definition token: " + token);
            }
            String config = null;

            if (pair.length == 2) {
                config = StringUtils.clean(pair[1]);
                //if there was an open bracket, it assumed there is a closing bracket, so strip it too:
                config = config.substring(0, config.length() - 1);
                config = StringUtils.clean(config);

                //backwards compatibility prior to implementing SHIRO-205:
                //prior to SHIRO-205 being implemented, it was common for end-users to quote the config inside brackets
                //if that config required commas.  We need to strip those quotes to get to the interior quoted definition
                //to ensure any existing quoted definitions still function for end users:
                if (config != null && config.startsWith("\"") && config.endsWith("\"")) {
                    String stripped = config.substring(1, config.length() - 1);
                    stripped = StringUtils.clean(stripped);

                    //if the stripped value does not have any internal quotes, we can assume that the entire config was
                    //quoted and we can use the stripped value.
                    if (stripped != null && stripped.indexOf('"') == -1) {
                        config = stripped;
                    }
                    //else:
                    //the remaining config does have internal quotes, so we need to assume that each comma delimited
                    //pair might be quoted, in which case we need the leading and trailing quotes that we stripped
                    //So we ignore the stripped value.
                }
            }
            
            return new String[]{name, config};

        } catch (Exception e) {
            String msg = "Unable to parse filter chain definition token: " + token;
            throw new ConfigurationException(msg, e);
        }
    }
	
	public Map<String, ClientUrlAccessFilter> getClientUrlAccessFilters() {
		return clientUrlAccessFilters;
	}

	public ClientUrlAccessFilter getDefaultClientUrlAccessFilter() {
		return defaultClientUrlAccessFilter;
	}
	
}

package com.penglecode.xmodule.common.web.security.oauth2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.util.StreamUtils;

/**
 * 通过RedisTokenStore来存取OAuth2令牌，其序列化使用Jackson2时的配置
 * 
 * @author 	pengpeng
 * @date	2019年2月21日 下午1:12:22
 */
public class OAuth2TokenJackson2Module extends SimpleModule {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2TokenJackson2Module.class);
	
	public OAuth2TokenJackson2Module() {
		this("OAuth2TokenModule");
	}

	public OAuth2TokenJackson2Module(String name) {
		super(name);
		addSerializer(new OAuth2RefreshTokenJsonSerializer());
		addSerializer(new AbstractAuthenticationTokenSerializer());
		addDeserializer(OAuth2RefreshToken.class, new OAuth2RefreshTokenJsonDeserializer());
		addDeserializer(GrantedAuthority.class, new GrantedAuthorityJsonDeserializer());
		addDeserializer(OAuth2Authentication.class, new OAuth2AuthenticationJsonDeserializer());
		addDeserializer(PreAuthenticatedAuthenticationToken.class, new PreAuthenticatedAuthenticationTokenJsonDeserializer());
		addDeserializer(UsernamePasswordAuthenticationToken.class, new UsernamePasswordAuthenticationTokenJsonDeserializer());
		addDeserializer(RememberMeAuthenticationToken.class, new RememberMeAuthenticationTokenJsonDeserializer());
		addDeserializer(AnonymousAuthenticationToken.class, new AnonymousAuthenticationTokenJsonDeserializer());
		
		addDeserializer(WebAuthenticationDetails.class, new WebAuthenticationDetailsJsonDeserializer());
	}

	public static class GrantedAuthorityJsonDeserializer extends JsonDeserializer<GrantedAuthority> {

		@Override
		public GrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			ObjectCodec codec = p.getCodec();
			JsonNode rootNode = codec.readTree(p);
			return new SimpleGrantedAuthority(rootNode.get("authority").asText());
		}
		
	}
	
	public static class OAuth2RefreshTokenJsonSerializer extends JsonSerializer<OAuth2RefreshToken> {
		
		@Override
		public void serialize(OAuth2RefreshToken value, JsonGenerator jgen, SerializerProvider serializers)
				throws IOException {
			jgen.writeStartObject();
			jgen.writeStringField("value", value.getValue());
			if(value instanceof ExpiringOAuth2RefreshToken) {
				Date expiration = ((ExpiringOAuth2RefreshToken) value).getExpiration();
				jgen.writeStringField("expiration", expiration.toInstant().toString());
			}
			jgen.writeEndObject();
		}

		@Override
		public Class<OAuth2RefreshToken> handledType() {
			return OAuth2RefreshToken.class;
		}
		
	}
	
	public static class OAuth2RefreshTokenJsonDeserializer extends JsonDeserializer<OAuth2RefreshToken> {

		@Override
		public OAuth2RefreshToken deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			ObjectCodec codec = p.getCodec();
			JsonNode rootNode = codec.readTree(p);
			OAuth2RefreshToken refreshToken = null;
			if(rootNode.hasNonNull("expiration")) {
				String value = rootNode.get("value").asText();
				String expiration = rootNode.get("expiration").asText();
				refreshToken = new DefaultExpiringOAuth2RefreshToken(value, new Date(Instant.parse(expiration).toEpochMilli()));
			} else {
				String value = rootNode.get("value").asText();
				refreshToken = new DefaultOAuth2RefreshToken(value);
			}
			return refreshToken;
		}
		
	}
	
	public static class AbstractAuthenticationTokenSerializer extends JsonSerializer<AbstractAuthenticationToken> {

		private final Field authenticatedField = ReflectionUtils.findField(AbstractAuthenticationToken.class, "authenticated");
		
		@Override
		public void serialize(AbstractAuthenticationToken value, JsonGenerator jgen, SerializerProvider serializers)
				throws IOException {
			jgen.writeStartObject();
			jgen.writeStringField("authenticationTokenClass", value.getClass().getName());
			jgen.writeObjectField("authorities", new HashSet<GrantedAuthority>(value.getAuthorities()));
			jgen.writeObjectField("details", value.getDetails());
			jgen.writeStringField("detailsClass", value.getDetails() == null ? null : value.getDetails().getClass().getName());
			jgen.writeBooleanField("authenticated", getAuthenticated(value));
			if(value instanceof OAuth2Authentication) {
				serializeOAuth2Authentication((OAuth2Authentication) value, jgen, serializers);
			} else if (value instanceof PreAuthenticatedAuthenticationToken) {
				serializePreAuthenticatedAuthenticationToken((PreAuthenticatedAuthenticationToken) value, jgen, serializers);
			} else if (value instanceof UsernamePasswordAuthenticationToken) {
				serializeUsernamePasswordAuthenticationToken((UsernamePasswordAuthenticationToken) value, jgen, serializers);
			} else if (value instanceof RememberMeAuthenticationToken) {
				serializeRememberMeAuthenticationToken((RememberMeAuthenticationToken) value, jgen, serializers);
			} else if (value instanceof AnonymousAuthenticationToken) {
				serializeAnonymousAuthenticationToken((AnonymousAuthenticationToken) value, jgen, serializers);
			} else {
				serializeOtherAuthenticationToken(value, jgen, serializers);
			}
			jgen.writeEndObject();
		}
		
		protected void serializeOtherAuthenticationToken(AbstractAuthenticationToken value, JsonGenerator jgen, SerializerProvider serializers) {
			LOGGER.warn("Found Unresolved AuthenticationToken<{}> : {}", value.getClass(), value);
		}
		
		protected void serializeOAuth2Authentication(OAuth2Authentication value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
			jgen.writeObjectField("storedRequest", getStoredRequest(value.getOAuth2Request()));
			jgen.writeObjectField("userAuthentication", value.getUserAuthentication());
		}
		
		protected void serializePreAuthenticatedAuthenticationToken(PreAuthenticatedAuthenticationToken value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
			jgen.writeObjectField("principal", value.getPrincipal());
			jgen.writeStringField("principalClass", value.getPrincipal() == null ? null : value.getPrincipal().getClass().getName());
			
			jgen.writeObjectField("credentials", value.getCredentials());
			jgen.writeStringField("credentialsClass", value.getCredentials() == null ? null : value.getCredentials().getClass().getName());
		}
		
		protected void serializeUsernamePasswordAuthenticationToken(UsernamePasswordAuthenticationToken value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
			jgen.writeObjectField("principal", value.getPrincipal());
			jgen.writeStringField("principalClass", value.getPrincipal() == null ? null : value.getPrincipal().getClass().getName());
			
			jgen.writeObjectField("credentials", value.getCredentials());
			jgen.writeStringField("credentialsClass", value.getCredentials() == null ? null : value.getCredentials().getClass().getName());
		}
		
		protected void serializeRememberMeAuthenticationToken(RememberMeAuthenticationToken value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
			jgen.writeObjectField("principal", value.getPrincipal());
			jgen.writeStringField("principalClass", value.getPrincipal() == null ? null : value.getPrincipal().getClass().getName());
			
			jgen.writeNumberField("keyHash", value.getKeyHash());
		}
		
		protected void serializeAnonymousAuthenticationToken(AnonymousAuthenticationToken value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
			jgen.writeObjectField("principal", value.getPrincipal());
			jgen.writeStringField("principalClass", value.getPrincipal() == null ? null : value.getPrincipal().getClass().getName());
			
			jgen.writeNumberField("keyHash", value.getKeyHash());
		}
		

		protected boolean getAuthenticated(AbstractAuthenticationToken value) {
			//OAuth2Authentication.isAuthenticated被重写了,只能通过反射获取真的属性authenticated值
			return ReflectionUtils.getFieldValue(authenticatedField, value);
		}
		
		protected Map<String,Object> getStoredRequest(OAuth2Request oauthRequest) {
			Map<String,Object> mapRequest = new LinkedHashMap<String,Object>();
			mapRequest.put("resourceIds", oauthRequest.getResourceIds());
			mapRequest.put("authorities", oauthRequest.getAuthorities());
			mapRequest.put("approved", oauthRequest.isApproved());
			mapRequest.put("refresh", getTokenRequest(oauthRequest.getRefreshTokenRequest()));
			mapRequest.put("redirectUri", oauthRequest.getRedirectUri());
			mapRequest.put("responseTypes", oauthRequest.getResponseTypes());
			mapRequest.put("extensions", oauthRequest.getExtensions());
			
			mapRequest.put("clientId", oauthRequest.getClientId());
			mapRequest.put("scope", new HashSet<String>(oauthRequest.getScope()));
			mapRequest.put("requestParameters", new HashMap<String,String>(oauthRequest.getRequestParameters()));
			return mapRequest;
		}
		
		protected Map<String,Object> getTokenRequest(TokenRequest tokenRequest) {
			Map<String,Object> mapRequest = null;
			if(tokenRequest != null) {
				mapRequest = new LinkedHashMap<String,Object>();
				mapRequest.put("grantType", tokenRequest.getGrantType());
				mapRequest.put("clientId", tokenRequest.getClientId());
				mapRequest.put("scope", new HashSet<String>(tokenRequest.getScope()));
				mapRequest.put("requestParameters", new HashMap<String,String>(tokenRequest.getRequestParameters()));
			}
			return mapRequest;
		}
		
		@Override
		public Class<AbstractAuthenticationToken> handledType() {
			return AbstractAuthenticationToken.class;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public abstract static class AbstractAuthenticationTokenJsonDeserializer<T extends AbstractAuthenticationToken> extends JsonDeserializer<T> {

		private final Field refreshField = ReflectionUtils.findField(OAuth2Request.class, "refresh");
		
		private final Class<T> authenticationTokenClass;
		
		public AbstractAuthenticationTokenJsonDeserializer(Class<T> authenticationTokenClass) {
			super();
			this.authenticationTokenClass = authenticationTokenClass;
		}

		public Class<T> getAuthenticationTokenClass() {
			return authenticationTokenClass;
		}

		public Field getRefreshField() {
			return refreshField;
		}

		@Override
		public T deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			ObjectCodec codec = jp.getCodec();
			JsonNode rootNode = codec.readTree(jp);
			
			Object details = null;
			if(rootNode.hasNonNull("details")) {
				Class<?> detailsClass = ClassUtils.forName(rootNode.get("detailsClass").asText());
				details = deserializeDetails(detailsClass, jp, rootNode, rootNode.get("details"));
			}
			
			Class<? extends AbstractAuthenticationToken> authenticationTokenClass = (Class<? extends AbstractAuthenticationToken>) ClassUtils.forName(rootNode.get("authenticationTokenClass").asText());
			
			AbstractAuthenticationToken authenticationToken = null;
			
			if(authenticationTokenClass.equals(OAuth2Authentication.class)) {
				authenticationToken = deserializeOAuth2Authentication(jp, ctxt, rootNode, authenticationTokenClass);
			} else if(authenticationTokenClass.equals(PreAuthenticatedAuthenticationToken.class)) {
				authenticationToken = deserializePreAuthenticatedAuthenticationToken(jp, ctxt, rootNode, authenticationTokenClass);
			} else if(authenticationTokenClass.equals(UsernamePasswordAuthenticationToken.class)) {
				authenticationToken = deserializeUsernamePasswordAuthenticationToken(jp, ctxt, rootNode, authenticationTokenClass);
			} else if(authenticationTokenClass.equals(RememberMeAuthenticationToken.class)) {
				authenticationToken = deserializeRememberMeAuthenticationToken(jp, ctxt, rootNode, authenticationTokenClass);
			} else if(authenticationTokenClass.equals(AnonymousAuthenticationToken.class)) {
				authenticationToken = deserializeAnonymousAuthenticationToken(jp, ctxt, rootNode, authenticationTokenClass);
			} else {
				authenticationToken = deserializeOtherAuthenticationToken(jp, ctxt, rootNode, authenticationTokenClass);
			}
			if(authenticationToken != null) {
				authenticationToken.setDetails(details);
			}
			return (T) authenticationToken;
		}
		
		protected AbstractAuthenticationToken deserializeOtherAuthenticationToken(JsonParser jp, DeserializationContext ctxt, JsonNode rootNode, Class<? extends AbstractAuthenticationToken> authTokenClass) throws IOException, JsonProcessingException{
			LOGGER.warn("Found Unresolved AuthenticationToken<{}> : {}", authTokenClass, rootNode.toString());
			return null;
		}
		
		protected AbstractAuthenticationToken deserializeOAuth2Authentication(JsonParser jp, DeserializationContext ctxt, JsonNode rootNode, Class<? extends AbstractAuthenticationToken> authTokenClass) throws IOException, JsonProcessingException{
			ObjectCodec codec = jp.getCodec();
			ObjectNode objectNode = (ObjectNode) rootNode.get("storedRequest");
			
			Map<String,String> requestParameters = codec.readValue(getTraversedJsonParser(objectNode.get("requestParameters"), codec), new TypeReference<Map<String,String>>(){});
			String clientId = objectNode.get("clientId").asText();
			Collection<GrantedAuthority> authorities = codec.readValue(getTraversedJsonParser(objectNode.get("authorities"), codec), new TypeReference<Set<GrantedAuthority>>(){});
			boolean approved = objectNode.get("approved").asBoolean();
			Set<String> scope = StreamUtils.asStream(objectNode.get("scope").elements()).map(JsonNode::asText).collect(Collectors.toSet());
			Set<String> resourceIds = StreamUtils.asStream(objectNode.get("resourceIds").elements()).map(JsonNode::asText).collect(Collectors.toSet());
			String redirectUri = objectNode.get("redirectUri").asText();
			Set<String> responseTypes = StreamUtils.asStream(objectNode.get("responseTypes").elements()).map(JsonNode::asText).collect(Collectors.toSet());
			Map<String,Serializable> extensions = codec.readValue(getTraversedJsonParser(objectNode.get("extensions"), codec), new TypeReference<Map<String,Serializable>>(){});
			
			TokenRequest refresh = null;
			if(objectNode.hasNonNull("refresh")) {
				objectNode = (ObjectNode) objectNode.get("refresh");
				String rclientId = objectNode.get("clientId").asText();
				String grantType = objectNode.get("grantType").asText();
				Set<String> rscope = StreamUtils.asStream(objectNode.get("scope").elements()).map(JsonNode::asText).collect(Collectors.toSet());
				Map<String,String> rrequestParameters = codec.readValue(getTraversedJsonParser(objectNode.get("requestParameters"), codec), new TypeReference<Map<String,String>>(){});
				refresh = new TokenRequest(rrequestParameters, rclientId, rscope, grantType);
			}
			
			OAuth2Request storedRequest = new OAuth2Request(requestParameters, clientId, authorities, approved, scope, resourceIds, redirectUri, responseTypes, extensions);
			if(refresh != null) {
				ReflectionUtils.setFieldValue(refreshField, storedRequest, refresh);
			}
			
			AbstractAuthenticationToken userAuthentication = null;
			if(rootNode.hasNonNull("userAuthentication")) {
				objectNode = (ObjectNode) rootNode.get("userAuthentication");
				Class<? extends AbstractAuthenticationToken> authenticationTokenClass = (Class<? extends AbstractAuthenticationToken>) ClassUtils.forName(objectNode.get("authenticationTokenClass").asText());
				userAuthentication = codec.readValue(getTraversedJsonParser(objectNode, codec), authenticationTokenClass);
			}
			AbstractAuthenticationToken authenticationToken = new OAuth2Authentication(storedRequest, userAuthentication);
			authenticationToken.setAuthenticated(rootNode.get("authenticated").asBoolean());
			return authenticationToken;
		}
		
		protected AbstractAuthenticationToken deserializePreAuthenticatedAuthenticationToken(JsonParser jp, DeserializationContext ctxt, JsonNode rootNode, Class<? extends AbstractAuthenticationToken> authTokenClass) throws IOException, JsonProcessingException{
			ObjectCodec codec = jp.getCodec();
			Collection<GrantedAuthority> authorities = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("authorities"), codec), new TypeReference<Set<GrantedAuthority>>(){});
			
			Object principal = null;
			if(rootNode.hasNonNull("principal")) {
				Class<?> principalClass = ClassUtils.forName(rootNode.get("principalClass").asText());
				principal = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("principal"), codec), principalClass);
			}
			Object credentials = null;
			if(rootNode.hasNonNull("credentials")) {
				Class<?> credentialsClass = ClassUtils.forName(rootNode.get("credentialsClass").asText());
				credentials = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("credentials"), codec), credentialsClass);
			}
			AbstractAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(principal, credentials, authorities);
			authenticationToken.setAuthenticated(rootNode.get("authenticated").asBoolean());
			return authenticationToken;
		}
		
		protected AbstractAuthenticationToken deserializeUsernamePasswordAuthenticationToken(JsonParser jp, DeserializationContext ctxt, JsonNode rootNode, Class<? extends AbstractAuthenticationToken> authTokenClass) throws IOException, JsonProcessingException{
			ObjectCodec codec = jp.getCodec();
			Collection<GrantedAuthority> authorities = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("authorities"), codec), new TypeReference<Set<GrantedAuthority>>(){});
			
			Object principal = null;
			if(rootNode.hasNonNull("principal")) {
				Class<?> principalClass = ClassUtils.forName(rootNode.get("principalClass").asText());
				principal = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("principal"), codec), principalClass);
			}
			Object credentials = null;
			if(rootNode.hasNonNull("credentials")) {
				Class<?> credentialsClass = ClassUtils.forName(rootNode.get("credentialsClass").asText());
				credentials = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("credentials"), codec), credentialsClass);
			}
			AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
			return authenticationToken;
		}
		
		protected AbstractAuthenticationToken deserializeRememberMeAuthenticationToken(JsonParser jp, DeserializationContext ctxt, JsonNode rootNode, Class<? extends AbstractAuthenticationToken> authTokenClass) throws IOException, JsonProcessingException{
			ObjectCodec codec = jp.getCodec();
			Collection<GrantedAuthority> authorities = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("authorities"), codec), new TypeReference<Set<GrantedAuthority>>(){});
			Object principal = null;
			if(rootNode.hasNonNull("principal")) {
				Class<?> principalClass = ClassUtils.forName(rootNode.get("principalClass").asText());
				principal = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("principal"), codec), principalClass);
			}
			int keyHash = rootNode.get("keyHash").asInt();
			AbstractAuthenticationToken authenticationToken = BeanUtils.instantiateClass(ClassUtils.getConstructor(RememberMeAuthenticationToken.class, new Class<?>[] {Integer.class, Object.class, Collection.class}), keyHash, principal, authorities);
			authenticationToken.setAuthenticated(rootNode.get("authenticated").asBoolean());
			return authenticationToken;
		}
		
		protected AbstractAuthenticationToken deserializeAnonymousAuthenticationToken(JsonParser jp, DeserializationContext ctxt, JsonNode rootNode, Class<? extends AbstractAuthenticationToken> authTokenClass) throws IOException, JsonProcessingException{
			ObjectCodec codec = jp.getCodec();
			Collection<GrantedAuthority> authorities = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("authorities"), codec), new TypeReference<Set<GrantedAuthority>>(){});
			Object principal = null;
			if(rootNode.hasNonNull("principal")) {
				Class<?> principalClass = ClassUtils.forName(rootNode.get("principalClass").asText());
				principal = jp.getCodec().readValue(getTraversedJsonParser(rootNode.get("principal"), codec), principalClass);
			}
			int keyHash = rootNode.get("keyHash").asInt();
			AbstractAuthenticationToken authenticationToken = BeanUtils.instantiateClass(ClassUtils.getConstructor(AnonymousAuthenticationToken.class, new Class<?>[] {Integer.class, Object.class, Collection.class}), keyHash, principal, authorities);
			authenticationToken.setAuthenticated(rootNode.get("authenticated").asBoolean());
			return authenticationToken;
		}
		
		protected Object deserializeDetails(Class<?> detailsClass, JsonParser jp, JsonNode rootNode, JsonNode detailsNode) throws IOException {
			ObjectCodec codec = jp.getCodec();
			return jp.getCodec().readValue(getTraversedJsonParser(detailsNode, codec), detailsClass);
		}
		
		protected JsonParser getTraversedJsonParser(JsonNode jsonNode, ObjectCodec codec) {
			JsonParser jsonParser = jsonNode.traverse();
			jsonParser.setCodec(codec);
			return jsonParser;
		}

		@Override
		public Class<?> handledType() {
			return getAuthenticationTokenClass();
		}

	}
	
	public static class OAuth2AuthenticationJsonDeserializer extends AbstractAuthenticationTokenJsonDeserializer<OAuth2Authentication> {

		public OAuth2AuthenticationJsonDeserializer() {
			super(OAuth2Authentication.class);
		}
		
	}
	
	public static class PreAuthenticatedAuthenticationTokenJsonDeserializer extends AbstractAuthenticationTokenJsonDeserializer<PreAuthenticatedAuthenticationToken> {

		public PreAuthenticatedAuthenticationTokenJsonDeserializer() {
			super(PreAuthenticatedAuthenticationToken.class);
		}
		
	}
	
	public static class UsernamePasswordAuthenticationTokenJsonDeserializer extends AbstractAuthenticationTokenJsonDeserializer<UsernamePasswordAuthenticationToken> {

		public UsernamePasswordAuthenticationTokenJsonDeserializer() {
			super(UsernamePasswordAuthenticationToken.class);
		}
		
	}
	
	public static class RememberMeAuthenticationTokenJsonDeserializer extends AbstractAuthenticationTokenJsonDeserializer<RememberMeAuthenticationToken> {

		public RememberMeAuthenticationTokenJsonDeserializer() {
			super(RememberMeAuthenticationToken.class);
		}
		
	}
	
	public static class AnonymousAuthenticationTokenJsonDeserializer extends AbstractAuthenticationTokenJsonDeserializer<AnonymousAuthenticationToken> {

		public AnonymousAuthenticationTokenJsonDeserializer() {
			super(AnonymousAuthenticationToken.class);
		}
		
	}
	
	public static class WebAuthenticationDetailsJsonDeserializer extends JsonDeserializer<WebAuthenticationDetails> {

		@Override
		public WebAuthenticationDetails deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			ObjectCodec codec = jp.getCodec();
			JsonNode rootNode = codec.readTree(jp);
			String remoteAddress = rootNode.get("remoteAddress").asText();
			String sessionId = null;
			if(rootNode.hasNonNull("sessionId")) {
				sessionId = rootNode.get("sessionId").asText();
			}
			return BeanUtils.instantiateClass(ClassUtils.getConstructor(WebAuthenticationDetails.class, new Class<?>[] {String.class, String.class}), remoteAddress, sessionId);
		}
		
	}
	
}
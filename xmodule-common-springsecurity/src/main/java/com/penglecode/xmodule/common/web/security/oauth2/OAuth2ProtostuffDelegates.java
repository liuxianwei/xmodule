package com.penglecode.xmodule.common.web.security.oauth2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;

import com.penglecode.xmodule.common.serializer.protostuff.ObjectWrapper;
import com.penglecode.xmodule.common.serializer.protostuff.ProtostuffSerializer;
import com.penglecode.xmodule.common.util.ReflectionUtils;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.WireFormat.FieldType;
import io.protostuff.runtime.Delegate;

/**
 * SpringSecurity-OAuth2 Token存储中基于Protostuff的序列化/反序列化时特殊类型的处理
 * 
 * 由于BaseRequest中的scope、requestParameters属性分别是unmodifiableSet,unmodifiableMap，且其setter方法不可见，
 * 因此Protostuff在BaseRequest反序列化时使用了反射获取了scope(unmodifiableSet)/requestParameters(unmodifiableMap)，然后进行add/put操作引发了UnsupportedOperationException异常
 * 此实现即在BaseRequest反序列化时通过显示调用有参构造方法来实例化BaseRequest对象
 * 
 * @author 	pengpeng
 * @date	2019年2月23日 下午5:12:18
 */
public class OAuth2ProtostuffDelegates {

	public static class OAuth2RequestDelegate implements Delegate<OAuth2Request> {

		private final Field refreshField = ReflectionUtils.findField(OAuth2Request.class, "refresh");
		
		@Override
		public FieldType getFieldType() {
			return FieldType.MESSAGE;
		}

		@Override
		public OAuth2Request readFrom(Input input) throws IOException {
			ObjectWrapper requestWrapper = new ObjectWrapper();
			input.mergeObject(requestWrapper, ProtostuffSerializer.SCHEMA);
			TempOAuth2Request temp = (TempOAuth2Request) requestWrapper.getObject();
			OAuth2Request oauth2Request = new OAuth2Request(temp.getRequestParameters(), temp.getClientId(), temp.getAuthorities(),
					temp.isApproved(), temp.getScope(), temp.getResourceIds(), temp.getRedirectUri(),
					temp.getResponseTypes(), temp.getExtensions());
			if(temp.getRefreshTokenRequest() != null) {
				ReflectionUtils.setFieldValue(refreshField, oauth2Request, temp.getRefreshTokenRequest());
			}
			return oauth2Request;
		}

		@Override
		public void writeTo(Output output, int number, OAuth2Request value, boolean repeated) throws IOException {
			ObjectWrapper requestWrapper = new ObjectWrapper();
			TempOAuth2Request temp = new TempOAuth2Request(value.getRequestParameters(), value.getClientId(), value.getAuthorities(),
					value.isApproved(), value.getScope(), value.getResourceIds(), value.getRedirectUri(),
					value.getResponseTypes(), value.getExtensions());
			if(value.getRefreshTokenRequest() != null) {
				ReflectionUtils.setFieldValue(refreshField, temp, value.getRefreshTokenRequest());
			}
			requestWrapper.setObject(temp);
			output.writeObject(number, requestWrapper, ProtostuffSerializer.SCHEMA, repeated);
		}

		@Override
		public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
			output.writeByteArray(number, input.readByteArray(), repeated);
		}

		@Override
		public Class<?> typeClass() {
			return OAuth2Request.class;
		}
		
		public Field getRefreshField() {
			return refreshField;
		}

		public static class TempOAuth2Request extends OAuth2Request {

			private static final long serialVersionUID = 1L;
			
			public TempOAuth2Request(Map<String, String> requestParameters, String clientId,
					Collection<? extends GrantedAuthority> authorities, boolean approved, Set<String> scope,
					Set<String> resourceIds, String redirectUri, Set<String> responseTypes,
					Map<String, Serializable> extensionProperties) {
				super(requestParameters, clientId, authorities, approved, scope, resourceIds, redirectUri, responseTypes,
						extensionProperties);
			}

		}
		
	}
	
	public static class TokenRequestDelegate implements Delegate<TokenRequest> {

		@Override
		public FieldType getFieldType() {
			return FieldType.MESSAGE;
		}

		@Override
		public TokenRequest readFrom(Input input) throws IOException {
			ObjectWrapper requestWrapper = new ObjectWrapper();
			input.mergeObject(requestWrapper, ProtostuffSerializer.SCHEMA);
			TempTokenRequest temp = (TempTokenRequest) requestWrapper.getObject();
			return new TokenRequest(temp.getRequestParameters(), temp.getClientId(), temp.getScope(), temp.getGrantType());
		}

		@Override
		public void writeTo(Output output, int number, TokenRequest value, boolean repeated) throws IOException {
			ObjectWrapper requestWrapper = new ObjectWrapper();
			TempTokenRequest temp = new TempTokenRequest(value.getRequestParameters(), value.getClientId(), value.getScope(), value.getGrantType());
			requestWrapper.setObject(temp);
			output.writeObject(number, requestWrapper, ProtostuffSerializer.SCHEMA, repeated);
		}

		@Override
		public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
			output.writeByteArray(number, input.readByteArray(), repeated);
		}

		@Override
		public Class<?> typeClass() {
			return TokenRequest.class;
		}
		
		public static class TempTokenRequest extends TokenRequest {

			private static final long serialVersionUID = 1L;

			public TempTokenRequest(Map<String, String> requestParameters, String clientId, Collection<String> scope,
					String grantType) {
				super(requestParameters, clientId, scope, grantType);
			}

		}
		
	}
	
}

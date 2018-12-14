package com.penglecode.xmodule.common.fabric.util;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Properties;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.fabric.FabricEnrollment;
import com.penglecode.xmodule.common.fabric.FabricUser;
import com.penglecode.xmodule.common.fabric.exception.FabricRuntimeException;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.URIUtils;

public class FabricUtils {

	private static final Pattern DOMAIN_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)+");
	
	static {
        Security.addProvider(new BouncyCastleProvider());
    }
	
	/**
	 * 创建HFCAClient
	 * @param caName
	 * @param caUrl
	 * @param caClientProperties
	 * @return
	 */
	public static HFCAClient createHfCaClient(String caName, String caUrl, Properties caClientProperties) {
		Assert.hasText(caUrl, "Parameter 'caUrl' must be required!");
		Assert.isTrue(URIUtils.isHttpURL(caUrl), "Parameter 'caUrl' must be a http(s) URL!");
		try {
    	   CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
           HFCAClient caClient = null;
           if(!StringUtils.isEmpty(caName)) {
        	   caClient = HFCAClient.createNewInstance(caName, caUrl, caClientProperties);
           } else {
        	   caClient = HFCAClient.createNewInstance(caUrl, caClientProperties);
           }
           caClient.setCryptoSuite(cryptoSuite);
           return caClient;
       } catch (Exception e) {
    	   throw new FabricRuntimeException(e);
       }
    }
	
	/**
	 * 创建HFClient
	 * @return
	 */
	public static HFClient createHfClient() {
        try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			HFClient client = HFClient.createNewInstance();
			client.setCryptoSuite(cryptoSuite);
			return client;
		} catch (Exception e) {
			throw new FabricRuntimeException(e);
		}
    }
	
	/**
	 * 通过本地CA配置文件获取已登记用户
	 * @param userName
	 * @param orgName
	 * @param mspId
	 * @param privateKeyFile
	 * @param certificateFile
	 * @return
	 */
	public static FabricUser getUser(String userName, String orgName, String mspId, File privateKeyFile, File certificateFile) {
		try {
			FabricUser unenrolledUser = new FabricUser();
			unenrolledUser.setName(userName);
			unenrolledUser.setAffiliation(orgName);
			unenrolledUser.setMspId(mspId);
			String cert = FileUtils.readFileToString(certificateFile, GlobalConstants.DEFAULT_CHARSET);
			PrivateKey key = FabricUtils.toPrivateKey(FileUtils.readFileToString(privateKeyFile, GlobalConstants.DEFAULT_CHARSET));
			unenrolledUser.setEnrollment(new FabricEnrollment(key, cert));
			return unenrolledUser;
		} catch (Exception e) {
			throw new FabricRuntimeException(e);
		}
	}
	
	/**
	 * java.security.PrivateKey转String
	 * @param privateKey
	 * @return
	 */
	public static String toString(PrivateKey privateKey) {
		Assert.notNull(privateKey, "Parameter 'privateKey' can not be null!");
		try {
			StringWriter pemStrWriter = new StringWriter();
			JcaPEMWriter pemWriter = new JcaPEMWriter(pemStrWriter);
			pemWriter.writeObject(privateKey);
			pemWriter.close();
			return pemStrWriter.toString();
		} catch (Exception e) {
			throw new FabricRuntimeException(e);
		}
	}
	
	/**
	 * String转java.security.PrivateKey
	 * @param stringKey
	 * @return
	 */
	public static PrivateKey toPrivateKey(String stringKey) {
		Assert.hasText(stringKey, "Parameter 'stringKey' can not be empty!");
		try {
			Reader pemReader = new StringReader(stringKey);
			Object keyObject = null;
			PrivateKeyInfo privateKeyInfo = null;
			try (PEMParser pemParser = new PEMParser(pemReader)) {
				keyObject = pemParser.readObject();
				if(keyObject instanceof PrivateKeyInfo) {
					privateKeyInfo = (PrivateKeyInfo) keyObject;
				} else if (keyObject instanceof PEMKeyPair) {
					privateKeyInfo = ((PEMKeyPair) keyObject).getPrivateKeyInfo();
				} else {
					throw new FabricRuntimeException("Unrecognized key object : " + keyObject);
				}
			}
			return new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(privateKeyInfo);
		} catch (Exception e) {
			throw new FabricRuntimeException(e);
		}
	}
	
	/**
	 * 从peerName,ordererName中获取域名
	 * @param name
	 * @return
	 */
	public static String getDomainName(final String name) {
		Assert.hasText(name, "Parameter 'name' can not be empty!");
        int dot = name.indexOf(".");
        if (-1 == dot) {
            return null;
        } else {
            return name.substring(dot + 1);
        }
    }
	
	/**
	 * 判断名称是否是域名格式的
	 * @param name
	 * @return
	 */
	public static boolean isDomainName(String name) {
		if(name != null) {
			return DOMAIN_PATTERN.matcher(name).matches();
		}
		return false;
	}
	
}

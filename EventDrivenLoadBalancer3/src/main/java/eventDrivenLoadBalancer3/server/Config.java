package eventDrivenLoadBalancer3.server;

import java.util.List;

import eventDrivenLoadBalancer3.Balancer.Balancer_Algorithm;

public class Config {
	private Integer timeout = 0;
	private String host;
	private Integer port;
	private Integer restPort;
	private Balancer_Algorithm algorithm;
	private String protocol;
	
	/*
	 * Whether or not the config is listening
	 * for new socket connections to load balance
	 * between the child configs. 
	 */
	private boolean listening;
	
	
	private ConfigLoader listensFor; 
	private String keystorePath = "";
	private String keystorePassword = "";
	private String keystoreType = "";
	private String keyAlgorithm = "";
	private int ballances = 0;
	private List<Config> childConfig;
	
	public Config() {
		
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Balancer_Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Balancer_Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean isListening() {
		return listening;
	}

	public void setListening(boolean listening) {
		this.listening = listening;
	}

	public ConfigLoader getListensFor() {
		return listensFor;
	}

	public void setListensFor(ConfigLoader listensFor) {
		this.listensFor = listensFor;
	}

	public String getKeystorePath() {
		return keystorePath;
	}

	public void setKeystorePath(String keystorePath) {
		this.keystorePath = keystorePath;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeystoreType() {
		return keystoreType;
	}

	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}

	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
	}

	public int getBallances() {
		return ballances;
	}

	public void setBallances(int ballances) {
		this.ballances = ballances;
	}

	public Integer getRestPort() {
		return restPort;
	}

	public void setRestPort(Integer restPort) {
		this.restPort = restPort;
	}

	public List<Config> getChildConfig() {
		return childConfig;
	}

	public void setChildConfig(List<Config> childConfig) {
		this.childConfig = childConfig;
	}
	
	/**
	 * compares two {@link Config}'s to see if the
	 * hosts and ports are the same.
	 * 
	 * @param c config to compare
	 * @return
	 */
	public boolean sameConnection(Config c) {
		return c.getHost().equals(this.getHost()) && c.getPort().equals(this.getPort());
	}
	
	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		
		if(o instanceof Config) {
			Config c = (Config) o;
			
			ret = this.sameConnection(c);
			ret = ret && (this.listening == c.isListening());
			ret = ret && this.algorithm.equals(c.getAlgorithm()) && this.keyAlgorithm.equals(c.getKeyAlgorithm());
			ret = ret && this.keystoreType.equals(c.getKeystoreType()) && this.getKeystorePassword().equals(c.getKeystorePassword());
			ret = ret && this.keystorePath.equals(c.getKeystorePath()) && (this.ballances == c.getBallances());
			ret = ret && this.protocol.equals(c.getProtocol()) && this.restPort.equals(c.getRestPort());
			ret = ret && this.getHost().equals(c.getHost())&& this.timeout.equals(c.getTimeout());
		}
		
		return ret;
	}

}

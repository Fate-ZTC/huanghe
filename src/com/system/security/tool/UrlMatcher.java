package com.system.security.tool;


public interface UrlMatcher {
	public abstract Object compile(String paramString);
	public abstract boolean pathMatchesUrl(Object paramObject ,String paramString);
	public abstract String getUniveralMathPattern();
	public abstract boolean requiresLowerCaseUrl();
}

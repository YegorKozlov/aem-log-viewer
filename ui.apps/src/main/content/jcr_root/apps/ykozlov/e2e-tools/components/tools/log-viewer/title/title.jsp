<%@page session="false"
          import="org.apache.sling.api.resource.Resource,
          org.apache.sling.api.resource.NonExistingResource,
			org.apache.sling.api.resource.ValueMap" %>
<%@include file="/libs/granite/ui/global.jsp" %>
<%
	String title = i18n.get("Health Reports");
	String suffix = slingRequest.getRequestPathInfo().getSuffix();

    if (suffix != null && suffix.trim().length() > 0) {
        Resource res = resourceResolver.getResource(suffix);
        if(res != null && !(res instanceof NonExistingResource)) {
            ValueMap vm = res.getValueMap();
            title = vm.get("name", "") + ": " + vm.get("host", "");
    	}
    }

%><div><%= outVar(xssAPI, i18n, title) %></div>

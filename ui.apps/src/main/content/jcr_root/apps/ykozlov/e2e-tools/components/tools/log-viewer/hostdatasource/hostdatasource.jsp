<%@page session="false"
        import="org.apache.sling.api.resource.Resource,
        org.apache.sling.api.resource.NonExistingResource,
        org.apache.sling.api.resource.ResourceUtil,
        com.adobe.granite.ui.components.Config,
        com.adobe.granite.ui.components.ExpressionHelper,
        org.apache.sling.api.resource.ValueMap,
        com.adobe.granite.ui.components.PagingIterator,
        org.apache.sling.api.wrappers.ValueMapDecorator,
        com.adobe.granite.ui.components.ds.*,
        org.apache.jackrabbit.commons.JcrUtils,
        java.util.*,
        javax.jcr.Session,
        org.osgi.service.cm.ConfigurationAdmin" %>
<%@include file="/libs/granite/ui/global.jsp"%>
<%

    String path = "/var/e2e/logs";
	if(resourceResolver.getResource(path) == null){
		JcrUtils.getOrCreateByPath(path, "sling:Folder", resourceResolver.adaptTo(Session.class));
	    resourceResolver.commit();
	}
	Resource cfgRoot = resourceResolver.getResource(path);
    ExpressionHelper ex = cmp.getExpressionHelper();

     // datasource configuration info
    Config cfg = new Config(resource.getChild(Config.DATASOURCE));
    Integer offset = ex.get(cfg.get("offset", String.class), Integer.class);
    Integer limit = ex.get(cfg.get("limit", String.class), Integer.class);


    DataSource ds = new AbstractDataSource() {
        public Iterator<Resource> iterator() {
            return new PagingIterator<Resource>(cfgRoot.listChildren(), offset, limit);
        }
    };

    request.setAttribute(DataSource.class.getName(), ds);
%>

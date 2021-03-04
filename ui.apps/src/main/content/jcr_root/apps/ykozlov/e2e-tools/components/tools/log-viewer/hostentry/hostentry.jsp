<%@page session="false"
        import="org.apache.sling.api.resource.ValueMap,
        com.adobe.granite.ui.components.Tag,
        com.adobe.granite.ui.components.AttrBuilder" %>
<%@include file="/libs/granite/ui/global.jsp" %>
<%

    String contextPath = request.getContextPath();
    ValueMap valueMap = resource.adaptTo(ValueMap.class);
	String name = valueMap.get("name", "");
	String url = valueMap.get("host", "");
	String icon = "viewList";
	String smallIcon = "search";
	String path =  resource.getPath();
	String href = "/apps/e2e-tools/content/log-viewer/host.html" + path;

    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();

    attrs.addClass("foundation-collection-navigator");
    attrs.addClass("whitecard");
%>
<coral-card assetwidth="400" assetheight="380" <%= attrs %>
    data-foundation-collection-navigator-href="<%= href %>"
    title="<%=name%>"
	data-name="<%=name%>"
	data-host="<%=url%>"
	data-path="<%=path%>"
    colorhint="#FFFFFF">
    <coral-card-asset class="whitecard">
        <coral-icon icon="<%= icon %>" class="largeIcon centerText">
        </coral-icon>
    </coral-card-asset>
    <coral-card-content class="customCardContent">
        <coral-card-title class="customCardTitle">
            <table>
                <tr>
                    <td>
                        <div class="smallIcon">
                            <coral-icon icon="<%= smallIcon %>">
                            </coral-icon>
                        </div>
                    </td>
                    <td>
                        <%= name %>
                    </td>
                </tr>
            </table>
        </coral-card-title>
        <coral-card-propertylist class="propertyWithMargin">
            <coral-card-property class="coral3-Card-property"><coral-card-property-content>
               <%=url%>
            </coral-card-property-content></coral-card-property>
        </coral-card-propertylist>
    </coral-card-content>
</coral-card>
<coral-quickactions target="_prev">
    <coral-quickactions-item icon="check" class="foundation-collection-item-activator">Select</coral-quickactions-item>
    <coral-quickactions-item icon="gears" class="configureQuickAction"
    data-editpath="<%=path%>">Configure</coral-quickactions-item>
</coral-quickactions>

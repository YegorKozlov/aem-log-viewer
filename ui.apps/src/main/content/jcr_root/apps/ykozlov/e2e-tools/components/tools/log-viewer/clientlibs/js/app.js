/*
 * #%L
 * AEM Log Viewer
 * %%
 * Copyright (C) 2021 Yegor Kozlov
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
(function(document, $, Granite) {
    "use strict";

    var ui = $(window).adaptTo("foundation-ui");
    
    $(window).adaptTo("foundation-registry").register("foundation.collection.action.action", {
        name: "e2e-tools.dashboard.task.delete",
        handler: function(name, el, config, collection, selections) {	
            if (selections.length === 1) {
				var card = $(selections).find("coral-card");
                if(card.length > 0) {
					var taskName = card.data("name");
                    var message = "You are going to delete the following item:"
                    message += "<p><b>" + taskName + "</b></p>"
                }

                ui.prompt("Delete Host", message, "notice", [
					{
                        text: "Cancel"
                    }, 
                    {
                        text: "Delete",
                        warning: true,
                        handler: function() {
                            selections.map(function(item) {
                                return $.ajax({
                                    type: "DELETE",
                                    url: $(item).find("coral-card").data("path")
                                }).then(function() {
                                    window.location.reload(true);
                                });
                            });
                    	}	
                	}
                ]);
            }
        }
    });

    $(document).on("click", ".configureQuickAction", function(e) {
		var form = $(this).closest("coral-masonry-item").find("coral-card");
        var host = $(form).data("host");
        var name = $(form).data("name");
        var action = $(form).data("path");
        var  dlg = document.querySelector('#modalConfigureHost');
         $("#configureHost-name").val(name);
         $("#configureHost-host").val(host);
         $(dlg).find("#createHostForm").attr("action", action);
         dlg.show();
    });


})(document, Granite.$);

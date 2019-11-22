/*
 *
 *   Copyright 2018 org.jfaster.derror.
 *     <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   </p>
 */

$(function() {
    doLocale();
    renderModeCenters();
    bindButtons();
});

function renderModeCenters() {
    $("#user-centers").bootstrapTable({
        url: "/user/load",
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true
    }).on("all.bs.table", function() {
        doLocale();
    });
}

function generateOperationButtons(val, row) {
  var operationTd = "<button operation='edit-user-center' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' userId='" + row.id + "' data-lang='user-auth-edit'></button>&nbsp;";
  return operationTd;
}

function bindButtons() {
  bindUpdateButtons();
}

function bindUpdateButtons() {
  $(document).off("click", "button[operation='edit-user-center']");
  $(document).on("click", "button[operation='edit-user-center']", function(event) {
    var userId = $(event.currentTarget).attr("userId");
    $("#content").load("/user/goAuth?userId=" + userId);
  });
}
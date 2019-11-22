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
  var userId  = $("#userId").val();
  $("#user-auth-centers").bootstrapTable({
        url: "/user/auth/load",
        method: "GET",
        queryParams: {"userId": userId},
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true
    }).on("all.bs.table", function() {
        doLocale();
    });
}

/**
 * 格式化角色
 * @param value
 * @param row
 */
function formatRoleId(value, row) {
  var roles = row.roles;
  var result = "<select operator='select' data-tags=\"true\" data-placeholder=\"Select an option\" class=\"selectpicker show-tick form-control\" data-live-search=\"false\">"
  for(var i=0;i<roles.length;i++){
    var desc = $.i18n.prop("user-role-" + roles[i]);
    if (row.roleId == roles[i]) {
      result += " <option value=\"" + row.projectId + "_" + roles[i] + "\" selected='selected'>" + desc + "</option>";
    } else {
      result += " <option value=\"" + row.projectId + "_" + roles[i] + "\">" + desc + "</option>";
    }
  }
  result += "</select>";
  return result
}

/**
 * 设置应用
 * @param value
 * @param row
 */
function formatApps(value, row) {
  var authorizedApps = row.authorizedApps;
  if (authorizedApps) {
    var all = $.i18n.prop("app-list-all");
    var result = "<input opeartor='checkbox' type=\"checkbox\" id=\"inlineCheckbox_-1\" value=\"-1\" onclick='selectAll(this)' >&nbsp;" + all + "&nbsp;";
    for (var i=0; i<authorizedApps.length; i++) {
      var checked = authorizedApps[i].checked;
      var label = "<input opeartor='checkbox' onclick='selectOne(this)' type=\"checkbox\" id=\"inlineCheckbox_\" "
          + authorizedApps[i].id + " value=\"" + authorizedApps[i].projectId
          + "_" + authorizedApps[i].id + "\" >&nbsp;"
          + authorizedApps[i].appName + "&nbsp;";
      if (checked) {
        label = "<input checked='checked' opeartor='checkbox' onclick='selectOne(this)' type=\"checkbox\" id=\"inlineCheckbox_\" "
            + authorizedApps[i].id + " value=\"" + authorizedApps[i].projectId
            + "_" + authorizedApps[i].id + "\" >&nbsp;"
            + authorizedApps[i].appName + "&nbsp;";
      }
      result += label;
    }
    result += ""
    return result;
  }
}

/**
 * 全选
 */
function selectAll(target) {
  var apps = $("input[opeartor='checkbox']");
  if (target.checked) {
    apps.each(function () {
      $(this).attr('checked', true);
    });
  } else {
    apps.each(function () {
      $(this).removeAttr('checked');
    });
  }
}

/**
 * 单选框
 */
function selectOne(target) {
  $(target).attr('checked', true);
}

function bindButtons() {
  bindUpdateButtons();
}

function bindUpdateButtons() {
  $("#modify-auth-config").on("click", function (event) {
      var selectApp = "";
      var apps = $("input[opeartor='checkbox']");
      apps.each(function () {
        if ($(this).attr("checked") == 'checked' && $(this).val() != -1) {
          selectApp += $(this).val() + ",";
        }
      });
      var selectProject = "";
      var projects = $("select[operator='select']");
      projects.each(function () {
          selectProject += $(this).val() + ",";
      });
      var userId  = $("#userId").val();

      $.ajax({
        url: "/user/auth/update",
        type: "POST",
        data: JSON.stringify({"userId": userId, "selectProject": selectProject, "selectApp": selectApp}),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
          if (data) {
            showSuccessDialog();
          } else {
            showFailDialog($.i18n.prop("operation-fail"));
          }
        }
      });
  });
}
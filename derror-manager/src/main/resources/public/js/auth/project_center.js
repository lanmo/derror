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
    getAuth();
    validate();
    dealModeCenterModal();
    handleFieldValidator();
    submitModeCenter();
});

function renderModeCenters() {
    $("#project-centers").bootstrapTable({
        url: "/project/load",
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true
    }).on("all.bs.table", function() {
        doLocale();
    });
}

/**
 * 获取权限
 */
function getAuth() {
  $.ajax({
    url: "/user/auth",
    type: "GET",
    dataType: "json",
    success: function(data) {
      if (data) {
        var roleId = data.data;
        if (roleId != 1) {
          $("#add-project-config").hide();
        }
      }
    }
  });
}

function dealModeCenterModal() {
    $("#add-project-config").click(function() {
        $("#add-project-center").modal({backdrop: 'public', keyboard: true});
    });
    $("#close-add-reg-form").click(function() {
        $("#add-project-center").on("hide.bs.modal", function () {
            $("#project-center-form")[0].reset();
        });
        $("#project-center-form").data("bootstrapValidator").resetForm();
    });
}

function handleFieldValidator() {
    $("#name").focus(function() {
        $("#project-center-form").data("bootstrapValidator").enableFieldValidators("name", true);
    });
}

function submitModeCenter() {
    $("#add-project-center-btn").on("click", function(event) {
        var bootstrapValidator = $("#project-center-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
            var name = $("#name").val();
            var ext = $("#ext").val();
            $.ajax({
                url: "/project/add",
                type: "POST",
                data: JSON.stringify({"name": name, "ext": ext}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    if (data) {
                        $("#add-project-center").on("hide.bs.modal", function() {
                            $("#project-center-form")[0].reset();
                        });
                        $("#project-center-form").data("bootstrapValidator").resetForm();
                        $("#add-project-center").modal("hide");
                        $("#project-centers").bootstrapTable("refresh");
                        $(".modal-backdrop").remove();
                        $("body").removeClass("modal-open");
                    }
                }
            });
        }
    });
}

function validate() {
    $("#project-center-form").bootstrapValidator({
        message: "This value is not valid",
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("project-name-not-null")
                    },
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("project-name-length-limit")
                    },
                    callback: {
                        message: $.i18n.prop("project-name-existed"),
                        callback: function() {
                            var name = $("#name").val();
                            var result = true;
                            $.ajax({
                                url: "/project/existed",
                                type: "POST",
                                data: JSON.stringify({"name": name}),
                                contentType: "application/json",
                                async: false,
                                success: function(d) {
                                  if (d.data < 1) {
                                      result = true;
                                    } else {
                                      result = false;
                                    }
                                }
                            });
                            return result;
                        }
                    }
                }
            }
        }
    });
    $("#project-center-form").submit(function(event) {
        event.preventDefault();
    });
}

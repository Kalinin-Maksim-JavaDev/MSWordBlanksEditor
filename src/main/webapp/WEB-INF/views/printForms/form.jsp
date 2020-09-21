<%-- 
    Document   : card
    Created on : 31 авг. 2020 г., 11:07:19
    Author     : Kalinin Maksim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CICADA | Шаблон печати документа WORD</title>

        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.7 -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/bootstrap/dist/css/bootstrap.min.css">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/font-awesome/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/Ionicons/css/ionicons.min.css">
        <!-- Select2 -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/select2/dist/css/select2.min.css">
        <!-- DataTables -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
        <link href="bower_components/DataTables/FixedHeader-3.1.4/css/fixedHeader.dataTables.min.css" rel="stylesheet" type="text/css"/>
        <!-- Theme style -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/timepicker/bootstrap-timepicker.min.css">
        <link href="${pageContext.request.contextPath}/bower_components/bootstrap-datetimepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
        <!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
        <!-- Google Font -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">       
    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">
            <!-- HEADER -->
            <jsp:include page="../_header.jsp"></jsp:include>
                <!-- Left side column. contains the logo and sidebar -->
            <jsp:include page="../_leftmenu.jsp"></jsp:include>

                <div class="content-wrapper">
                    <section class="content-header">
                        <div class="form-group col-md-4">
                        <c:if test = "${form==null}">
                            <label>Новый шаблон печати</label>
                        </c:if>
                        <c:if test = "${form!=null}">
                            <label>Шаблон печати &quot${form.getName()}&quot</label>
                        </c:if>
                    </div>
                </section>
                <section class="content">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box box-primary">
                                <div class="box-header with-border">
                                    <form action="${pageContext.request.contextPath}/printForms/save?type=element" method="post" enctype="multipart/form-data">
                                        <li>
                                            <label class="description" for="element_GROUP">Группа</label>
                                            <select name="GROUPID">
                                                <option value="">...</option>
                                                <c:forEach items="${groups}" var="group">
                                                    <option 
                                                        <c:if test="${form.getGroup().getID()==group.getID()}">
                                                            selected
                                                        </c:if>
                                                        value="${group.getID()}">${group.getName()}</option>
                                                </c:forEach>
                                            </select>
                                        </li>
                                        <li>
                                            <label class="description" for="element_NAME">Имя:</label>
                                            <input id  = "element_NAME" name="NAME" value=${form.getName()}>

                                            <input id  = "element_ID" name="ID" hidden value=${form.getID()}>
                                        </li>
                                        <li>
                                            <label class="description" for="element_DESCRIPTION">Описание:</label>
                                            <input id  = "element_DESCRIPTION" name="DESCRIPTION" value=${form.getDescription()}>
                                        </li>
                                        <li><label class="description" for="element_FILEPATH">Шаблон WORD: <a href=${form.getFilePath()} target="_blank">${form.getFilePath()}</a></label>
                                            <input id  = "element_FILEPATH" name="element_FILEPATH" type="file" value="${form.getFilePath()}">
                                        </li>
                                        <li>
                                            <label class="description" for="element_QUERY">Запрос к БД </label>
                                            <div>
                                                <textarea id = "element_QUERY" name="QUERY" >${form.getQuery()}</textarea> 
                                            </div> 
                                        </li>

                                        <section>
                                            <h2 class="box-title">Поля автозамены:</h2>

                                            <input type="button" value="Добавить поле" onclick="addRow()"/>

                                            <table id="filedsTable"  class="table table-striped table-bordered table-hover h6 compact">
                                                <thead>
                                                    <tr>
                                                        <th>Имя</th>
                                                        <th>Вставка в шаблоне</th>
                                                        <th>Описание</th>
                                                    </tr>
                                                </thead>  
                                                <tbody>
                                                    <c:forEach items="${form.getFields()}" var="field">
                                                        <tr>                                
                                                            <td><input type=text name=FIELD.NAME value = "<c:out value="${field.getName()}" />" size=10></td>
                                                            <td><input type=text name=FIELD.MARK value = "<c:out value="${field.getMark()}" />" size=10></td>
                                                            <td><input type=text name=FIELD.DESCRIPTION value = "<c:out value="${field.getDescription()}" /> " size=10></td>                                
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <script>
                                                var d = document;

                                                update();
                                                function addRow()
                                                {
                                                    // Находим нужную таблицу
                                                    var tbody = d.getElementById('filedsTable').getElementsByTagName('TBODY')[0];

                                                    // Создаем строку таблицы и добавляем ее
                                                    var row = d.createElement("TR");
                                                    tbody.appendChild(row);

                                                    // Создаем ячейки в вышесозданной строке
                                                    // и добавляем тх
                                                    var td1 = d.createElement("TD");
                                                    var td2 = d.createElement("TD");
                                                    var td3 = d.createElement("TD");

                                                    row.appendChild(td1);
                                                    row.appendChild(td2);
                                                    row.appendChild(td3);

                                                    // Наполняем ячейки
                                                    td1.innerHTML = "<td><input type=text name=FIELD.NAME size=10></td>";
                                                    td2.innerHTML = "<td><input type=text name=FIELD.MARK size=10></td>";
                                                    td3.innerHTML = "<td><input type=text name=FIELD.DESCRIPTION size=100></td>";

                                                    //update();
                                                }


                                                function update() {
                                                    let procRows = filedsTable.querySelectorAll("tbody tr");
                                                    for (let i = 0; i < procRows.length; i++) {

                                                        procRows[i].innerHTML += '<td><button  title="Remove"></td>';
                                                    }


                                                    filedsTable.querySelector("tbody").addEventListener("click", function (e) {
                                                        if (e.target.nodeName == "BUTTON") {
                                                            let cell = e.target.parentNode;

                                                            filedsTable.deleteRow(cell.parentNode.rowIndex);
                                                        }
                                                    })

                                                }
                                            </script>

                                            <button class="btn bg-blue-gradient"  type="submit">Сохранить</button>
                                        </section>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
                </form>

                <form style="display: inline" >

                    <input type="text" hidden value="${form.getID()}" name="id">
                    <label class="description" for="element_PARAM">$p1:</label>
                    <input id  = "element_PARAM_1" name="PARAM">
                    <button type="submit" formaction="${pageContext.request.contextPath}/printForms/print" value=""
                            formmethod="get"
                            class="btn btn-xs bg-blue-gradient btn-flat " title="Печать документа">
                        </i> Пробная печать
                    </button>
                </form>
            </div>
            <!-- /.content-wrapper -->

            <!-- FOOTER -->
            <jsp:include page="../_footer_control.jsp"></jsp:include>
            <!-- /.control-sidebar -->
            <!-- Add the sidebar's background. This div must be placed
                 immediately after the control sidebar -->
            <div class="control-sidebar-bg"></div>
        </div>
        <!-- ./wrapper -->

        <!-- jQuery 3 -->
        <script src="bower_components/jquery/dist/jquery.min.js"></script>
        <!-- jQuery UI 1.11.4 -->
        <script src="bower_components/jquery-ui/jquery-ui.min.js"></script>
        <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
        <script>
                                                $.widget.bridge('uibutton', $.ui.button);
        </script>
        <!-- Bootstrap 3.3.7 -->
        <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
        <!-- Select2 -->
        <script src="bower_components/select2/dist/js/select2.full.min.js"></script>
        <!-- DataTables -->
        <script src="bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
        <script src="bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
        <script src="bower_components/DataTables/FixedHeader-3.1.4/js/dataTables.fixedHeader.min.js" type="text/javascript"></script>
        <!-- Slimscroll -->
        <script src="bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
        <!-- FastClick -->
        <script src="bower_components/fastclick/lib/fastclick.js"></script>
        <!-- AdminLTE App -->
        <script src="dist/js/adminlte.min.js"></script>
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="dist/js/pages/dashboard.js"></script>
        <!-- AdminLTE for demo purposes -->
        <script src="dist/js/demo.js"></script>
        <!-- bootstrap datepicker -->
        <script src="bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="bower_components/bootstrap-datepicker/js/locales/bootstrap-datepicker.ru.min.js" type="text/javascript"></script>
        <script src="bower_components/bootstrap-datetimepicker/php-date-formatter/js/php-date-formatter.min.js" type="text/javascript"></script>
        <script src="bower_components/bootstrap-datetimepicker/jquery.datetimepicker.js" type="text/javascript"></script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.maskedinput/1.4.1/jquery.maskedinput.min.js"></script>
    </body>
</html>

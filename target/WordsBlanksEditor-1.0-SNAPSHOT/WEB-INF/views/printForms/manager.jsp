<%-- 
    Document   : editor
    Created on : 2 сент. 2020 г., 8:40:11
    Author     : Kalinin Maksim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ru.rgsbank.cicada.beans.printForms.Forms" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CICADA | Печатные формы</title>

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
    <body class=""hold-transition skin-blue sidebar-mini">
        <div class="wrapper">
            <!-- HEADER -->
            <jsp:include page="../_header.jsp"></jsp:include>
                <!-- Left side column. contains the logo and sidebar -->
            <jsp:include page="../_leftmenu.jsp"></jsp:include>

                <!-- Content Wrapper. Contains page content -->
                <div class="content-wrapper">
                    <!-- Content Header (Page header) -->
                    <section class="content-header ">
                        <div class="form-group col-md-4">
                            <label>Печатные формы</label>
                        </div>
                    </section>

                    <!-- Main content -->
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box box-primary">
                                    <div class="box-header with-border">
                                        <div class="btn-group" role="group">
                                            <button type="button" onclick="newFormsGroup()"  class="btn btn-xs bg-blue-gradient">Добавить группу</button>
                                            <button type="button" onclick="newFormsElement()" class="btn btn-xs bg-blue-gradient">Добавить макет</button>

                                            <script type="text/javascript">
                                                function newFormsGroup() {
                                                    window.location = "${pageContext.request.contextPath}/printForms/newForm?type=group"
                                                }

                                                function newFormsElement() {
                                                    window.location = "${pageContext.request.contextPath}/printForms/newForm?type=element"
                                                }
                                        </script>
                                    </div>

                                    <table id="forms" class="table table-bordered table-hover h6 compact">
                                        <thead class="bg-blue-gradient">
                                            <tr>
                                                <th style="text-align:center;">Имя</th>
                                                <th style="text-align:center;">Описание</th>                                   
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${formGroupsList}" var="formGroup">
                                                <tr>                                
                                                    <td><a href=printForms<c:out value="${formGroup.getURL()}" />><b><c:out value="${formGroup.getName()}" /></b></td>
                                                </tr>
                                                <c:forEach items="${formsHierarchy.get(formGroup)}" var="form">
                                                    <c:if test="${!form.isGroup()}">
                                                    <tr>                                
                                                        <td class="box"><a href=printForms<c:out value="${form.getURL()}" />><c:out value="${form.getName()}" /></a></td>
                                                        <td class="box"><c:out value="${form.getDescription()}" /></td>                                
                                                    </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </c:forEach>
                                        </tbody>
                                    </table> 
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
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

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.maskedinput/1.4.1/jquery.maskedinput.min.js"></script>                    -->
    </body>
</html>
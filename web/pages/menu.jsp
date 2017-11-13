<%-- 
    Document   : menu
    Created on : 25/08/2017, 19:50:33
    Author     : allex
--%>

<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>PEAP: Portal Educacional para Alunos e Professores</title>

     <!--Bootstrap Core CSS--> 
    <link href="${pageContext.request.contextPath}/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

     <!--MetisMenu CSS--> 
    <link href="${pageContext.request.contextPath}/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

     <!--Custom CSS--> 
    <link href="${pageContext.request.contextPath}/dist/css/sb-admin-2.css" rel="stylesheet">

     <!--Custom Fonts--> 
    <link href="${pageContext.request.contextPath}/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!--Data Tables-->
    <link href="${pageContext.request.contextPath}/vendor/datatables/css/jquery.dataTables.css" rel="stylesheet" type="text/css">
    
    <!--Jquery-ui-->
    <link href="${pageContext.request.contextPath}/vendor/jquery-ui/jquery-ui.css" rel="stylesheet" type="text/css">
    
    <!--Tudo-->
    <link href="${pageContext.request.contextPath}/css/tudo.css" rel="stylesheet" type="text/css">
    
    
<!--     HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries 
     WARNING: Respond.js doesn't work if you view the page via file:// 
    [if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>
    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.html">PEAP</a>
            
        </div>
        <!-- /.navbar-header -->

        <ul class="nav navbar-top-links navbar-right">

            <!-- /.dropdown -->
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-user fa-fw"></i> ${sessionScope.username} <i class="fa fa-caret-down"></i>
                    
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li><a href="${pageContext.request.contextPath}/ProcessaLogout"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                    </li>
                </ul>
                <!-- /.dropdown-user -->
            </li>
            <!-- /.dropdown -->
        </ul>
        <!-- /.navbar-top-links -->

        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li>
                        <a href="${pageContext.request.contextPath}/ProcessaPost"><i class="fa fa-dashboard fa-fw"></i> Avisos</a>
                    </li>
                    <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                        <li class="menu-usuarios">
                            <a href="${pageContext.request.contextPath}/ProcessaUsuario"><i class="fa fa-user fa-fw"></i> Usuários</a>
                        </li>
                    </c:if>
                    <li>
                        <a href="${pageContext.request.contextPath}/ProcessaCurso"><i class="glyphicon glyphicon-education"></i> Cursos</a>
                    </li>
                    <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                        <li>
                            <a href="#"><i class="fa fa-sign-in"></i> Matrículas<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="${pageContext.request.contextPath}/ProcessaMatricula?function=curso"> Matrícula Curso</span></a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/ProcessaMatricula?function=disciplina"> Matrícula Disciplina</span></a>
                                </li>
                            </ul>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                        <li>
                            <a href="#"><i class="glyphicon glyphicon-edit"></i> Cadastros<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="${pageContext.request.contextPath}/ProcessaSemestre"> Semestres</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/ProcessaPeriodo"> Períodos</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/ProcessaDisciplina"> Disciplinas</a>
                                </li>
                            </ul>
                        </li>
                    </c:if>    

                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>
     <!--jQuery--> 
    <script src="${pageContext.request.contextPath}/vendor/jquery/jquery.js"></script>
    
    <!--Jquery-ui-->
    <script src="${pageContext.request.contextPath}/vendor/jquery-ui/jquery-ui.js"></script>

     <!--Bootstrap Core JavaScript--> 
    <script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.min.js"></script>

     <!--Metis Menu Plugin JavaScript--> 
    <script src="${pageContext.request.contextPath}/vendor/metisMenu/metisMenu.min.js"></script>
    
     <!--Custom Theme JavaScript--> 
    <script src="${pageContext.request.contextPath}/dist/js/sb-admin-2.js"></script>
    
    <!--Data Tables-->
    <script src="${pageContext.request.contextPath}/vendor/datatables/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/vendor/datatables-plugins/dataTables.bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/vendor/datatables-responsive/dataTables.responsive.js"></script>

</body>

</html>
<script language="javascript">
    $(document).ready(function(){
        
        $('table').DataTable({});

    });
</script>
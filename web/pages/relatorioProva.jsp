<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <div id="page-wrapper">
            
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">${lstRelatorio[0].prova.descricao}</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Usuário</th>
                                        <th>Nota</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${lstRelatorio}" var="x">
                                        <tr>
                                            <td>${x.usuario.nome}</td>
                                            <td>${x.notaFinal}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
 
    


<script type="text/javascript">
    
    $(document).ready(function(){   
                

 
    });
</script>
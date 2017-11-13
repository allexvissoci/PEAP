<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <div id="page-wrapper">
            
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Semestres</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <button type="button" class="btn btn-primary btn-novo" data-toggle="modal" data-target="#myModal" idfun="${x.id}">Novo</button>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Nome</th>
                                        <th>Opções</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listaSemestre}" var="x">
                                        <tr>
                                            <td>${x.semestreNome}</td>
                                            <td>
                                                <i style="font-size: 20px; color: #eea236;" idsemestre="${x.id}" class="fa fa-edit edit-semestre" data-toggle="modal" data-target="#myModal"></i>
                                                <i style="font-size: 20px; color: #d9534f;" idsemestre="${x.id}" class="glyphicon glyphicon-remove-circle delete-semestre"></i>
                                            </td>
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
    
    
    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Novo Semestre</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idSemestreHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="nome">Nome: <span style='color:red'>*</span></label>
                                <input type="text" class="form-control" name="nome" id="nome">
                            </div>
                        </form>
                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-semestre">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    


<script type="text/javascript">
    
    $(document).ready(function(){   
                
        $('.delete-semestre').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idsemestre');
                $.ajax({
                    type: "POST",
                    url: "ProcessaSemestre",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('semestre deletado com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
        });                
                
        $('.edit-semestre').on('click',function(){
            $('#myModal').find('.modal-title').html('Edição de Semestre');
            var id = $(this).attr('idsemestre');
            $.ajax({
                type: "POST",
                url: "ProcessaSemestre",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var semestre = $.parseJSON(data);
                    $("#idSemestreHide").val(semestre.id);
                    $("#nome").val(semestre.semestreNome);
                }
            });
        });
    
        
        $('.btn-save-semestre').on('click', function(){
            //verifica se tem id escondido
            var id = $("#idSemestreHide").val();
            var nome = $("#nome").val();

            if($.trim(nome)){
                                
                $.ajax({
                    type: "POST",
                    url: "ProcessaSemestre",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'nome': nome,
                    },
                    success: function (data) {
                        
                        if(!data){
                            alert("Dados não foram salvos!");
                        }
                        
                        $('#myModal').modal('hide');
                        location.reload();
                    }
                });

            }else{
                alert("Preencha os campos obrigatórios");
            }
            
        });
 
    });
</script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="wrapper">
    <jsp:include page="menu.jsp"/>
    <div id="page-wrapper">

        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">${disciplina.nome}</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="row">
            <div class="col-lg-12">
                <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                    <div class="new">
                        <span class="newoptions nova-materia" data-toggle="modal" data-target="#myModalMateria"><i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle"></i> Nova Matéria</span>
                        <span class="newoptions nova-prova" data-toggle="modal" data-target="#myModalProva"><i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle"></i> Nova Prova</span>
                    </div>
                </c:if>
                <!--PROVAS-->
                <div style="padding-top: 50px;">
                    <!-- /.row -->
                    <div class="row">
                        <c:forEach items="${listProvas}" var="prova">
                            <div class="col-lg-3 col-md-6">
                                <c:choose>
                                    <c:when test="${prova.resolvida eq true}">
                                        <c:set var="color" value="panel-primary" scope="page" />
                                    </c:when>    
                                    <c:otherwise>
                                        <c:set var="color" value="panel-yellow" scope="page" />
                                    </c:otherwise>
                                </c:choose>
                                <div class="panel ${color}">
                                    <div class="panel-heading">
                                        <div class="row">
                                            <div class="col-xs-3">
                                                <i class="fa fa-comments fa-5x"></i>
                                            </div>
                                            <div class="col-xs-9 text-right">
                                                <!--nota-->
                                                <div class="huge">
                                                    <c:if test="${prova.resolvida eq true}">
                                                        ${prova.notaFinal}
                                                    </c:if>
                                                </div>
                                                <div>${prova.descricao}</div>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="panel-footer">
                                        <i style="font-size: 20px; color: #337ab7;" podeEditar="${prova.podeEditar}" idProva="${prova.provaId}" resolvida="${prova.resolvida}" class="fa fa-eye detail-prova"></i>
                                        <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                                            <i style="font-size: 20px; color: #eea236;" idProva="${prova.provaId}" class="fa fa-edit edit-prova" data-toggle="modal" data-target="#myModalProva"></i>
                                            <i style="font-size: 20px; color: #d9534f;" idProva="${prova.provaId}" class="glyphicon glyphicon-remove-circle delete-prova"></i>
                                            <i style="font-size: 20px; color: #337ab7;" idProva="${prova.provaId}" class="fa fa-bar-chart-o relatorio-prova"></i>
                                        </c:if>
                                        <div class="clearfix"></div>
                                    </div>
                                    
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /.row -->
                </div>
                <!--/. PROVAS-->
                <!--MATERIAS-->
                <div>
                    <c:forEach items="${listMaterias}" var="materia">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4>${materia.materiaNome}
                                    <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                                        <i style="font-size: 20px; color: #eea236;" idMateria="${materia.materiaId}" class="fa fa-edit edit-materia" data-toggle="modal" data-target="#myModalMateria"></i>
                                        <i style="font-size: 20px; color: #d9534f;" idMateria="${materia.materiaId}" class="glyphicon glyphicon-remove-circle delete-materia"></i>
                                    </c:if>
                                </h4>
                            </div>
                            <!-- /.panel-heading -->
                            <div class="panel-body">
                                <form action="${pageContext.request.contextPath}/ProcessaArquivos?function=uploadArquivo&materiaId=${materia.materiaId}&disciplinaId=${disciplina.id}" method="POST" enctype="multipart/form-data">
                                    <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                                        <div>
                                            <label> Selecione o arquivo: </label>
                                            <input type="file" accept="image/" name="file"></input>
                                            <input type="submit" class="btn btn-primary" value="Enviar"></input>
                                        </div>
                                    </c:if>    

                                    <div class="list-group">
                                        <c:forEach items="${materia.arquivos}" var="arquivo">
                                            <div class="list-group-item">
                                                <a target="_blank" href="uploadFiles/${arquivo.arquivoId}_${arquivo.arquivoDescricao}" download="${arquivo.arquivoId}_${arquivo.arquivoDescricao}">
                                                    <i class="fa fa-download fa-fw"></i>
                                                    ${arquivo.arquivoDescricao}
                                                </a>
                                            <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                                                <i style="font-size: 20px; color: #d9534f; float:right;" idarquivo="${arquivo.arquivoId}" iddisciplina="${disciplina.id}" class="glyphicon glyphicon-remove-circle delete-arquivo"></i>
                                            </c:if>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </form>
                            </div>
                            <!-- /.panel-body -->
                        </div>
                        <!-- /.panel -->
                    </c:forEach>
                </div>
                <!--/. MATERIAS-->
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->    
    </div>
</div>
            
<!--MODAL PARA MATÉRIA-->            
<div class="modal fade" id="myModalMateria" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Nova Materia</h4>
            </div>
            <div class="modal-body">
                <form action='' method='post'>
                    <input type='text' class="hidden" id='idMateriaHide' value='0'></input>
                    <input type='text' class="hidden" id='idDisciplinaHide' value='${disciplina.id}'></input>
                    <div class="form-group has-feedback">
                        <label for="nome">Nome: <span style='color:red'>*</span></label>
                        <input type="text" class="form-control" name="nome" id="nome">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary btn-save-materia">Salvar</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->    



<!--MODAL PARA PROVA-->
<div class="modal fade" id="myModalProva" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Nova Prova</h4>
            </div>
            <div class="modal-body">                
                <form action='' method='post'>
                    <input type='text' class="hidden" id='idProvaHide' value='0'></input>
                    <input type='text' class="hidden" id='idDisciplinaProvaHide' value='${disciplina.id}'></input>
                    <div class="form-group has-feedback">
                        <label for="descricaoProva">Descrição: <span style='color:red'>*</span></label>
                        <input type="text" class="form-control" name="descricaoProva" id="descricaoProva">
                    </div>
                    <div class="form-group has-feedback">
                        <label for="observacao">Observações: </label>
                        <textarea rows="6" class="form-control" name="observacao" id="observacao"></textarea>
                    </div>
                    <div class="form-group has-feedback">
                        <label for="dataAplicacao">Data Aplicação: </label>
                        <input type="text" class="form-control datepicker" name="dataAplicacao" id="dataAplicacao">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary btn-save-prova">Salvar</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<a class="linkProvaDetail" href="${pageContext.request.contextPath}/ProcessaProva"></a>
<a class="linkRelatorioProvas" href="${pageContext.request.contextPath}/ProcessaProva"></a>

<script type="text/javascript">
    
    $(document).ready(function(){   
        $(".datepicker").datepicker({
            dateFormat: 'dd/mm/yy',
            dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
            dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
            dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
            monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
            monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
            nextText: 'Próximo',
            prevText: 'Anterior'
        });
        
        $('.detail-prova').on('click',function(){
            var id = $(this).attr('idProva');
            var resolvida = $(this).attr('resolvida');
            var podeEditar = $(this).attr('podeEditar');
            var href =$('.linkProvaDetail').attr('href');
            window.location.href = href+'?function=provaDetail&id='+id+'&resolvida='+resolvida+'&podeEditar='+podeEditar;
        });
        
        $('.relatorio-prova').on('click',function(){
            var id = $(this).attr('idProva');
            var href =$('.linkRelatorioProvas').attr('href');
            window.location.href = href+'?function=relatorioProva&id='+id;
            
        });
        
        $('.btn-save-prova').on('click',function(){
            var id = $("#idProvaHide").val();
            var disciplinaId = $("#idDisciplinaHide").val();
            var descricao = $('#descricaoProva').val();
            var observacao = $('#observacao').val();
            var dataAplicacao = $('#dataAplicacao').val();
            if((id && $.trim(descricao)) || !id && $.trim(descricao) != ""){
                $.ajax({
                    type: "POST",
                    url: "ProcessaProva",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'disciplinaId': disciplinaId,
                        'descricao': descricao,
                        'observacao': observacao,
                        'dataAplicacao': dataAplicacao
                    },
                    success: function (data) {
                        $('#myModalProva').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }
        });
        
        $('.edit-prova').on('click',function(){
            $('#myModalProva').find('.modal-title').html('Edição de Prova');
            
            var id = $(this).attr('idProva');
            $.ajax({
                type: "POST",
                url: "ProcessaProva",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var prova = $.parseJSON(data);
                    $("#idProvaHide").val(prova.id);
                    $("#descricaoProva").val(prova.descricao);
                    $("#dataAplicacao").val(prova.dataAplicacao);
                    
                }
            });
        });
        
        $('.delete-prova').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idProva');
                $.ajax({
                    type: "POST",
                    url: "ProcessaProva",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('Prova deletada com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
        });
        
    
        $('.btn-save-materia').on('click', function(){
            //verifica se tem id escondido
            var id = $("#idMateriaHide").val();
            var disciplinaId = $("#idDisciplinaHide").val();
            var nome = $("#nome").val();
            
            if((id && $.trim(nome)) || !id && $.trim(nome) != ""){
                $.ajax({
                    type: "POST",
                    url: "ProcessaMateria",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'nome': nome,
                        'disciplinaId': disciplinaId,
                    },
                    success: function (data) {
                        $('#myModalMateria').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }

        });
        
        $('.edit-materia').on('click',function(){
            $('#myModal').find('.modal-title').html('Edição de Matéria');
            
            var id = $(this).attr('idMateria');
            $.ajax({
                type: "POST",
                url: "ProcessaMateria",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var materia = $.parseJSON(data);
                    $("#idMateriaHide").val(materia.id);
                    $("#nome").val(materia.nome);
                }
            });
        });
        
        $('.delete-materia').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idMateria');
                $.ajax({
                    type: "POST",
                    url: "ProcessaMateria",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('Materia deletada com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
        });
        
        $('.delete-arquivo').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idarquivo');
                var iddisciplina = $(this).attr('iddisciplina');
                $.ajax({
                    type: "POST",
                    url: "ProcessaArquivos",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                        'iddisciplina':iddisciplina,
                    },
                    success: function (data) {
//                        var url = window.location.href;
//                        window.location.href = url;
                        location.reload();
                    }
                });
            }else{
                return false;
            }
            
        });   
        
    });
</script>


<style>
    .newoptions{
        display: block;
        text-align: center;
        width: 50%;
        float: left;
        padding: 5px 0;
    }
    
    input[type=file] {
        display: inline;
    }
</style>            
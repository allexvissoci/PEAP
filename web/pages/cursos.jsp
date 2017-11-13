<%-- 
    Document   : series
    Created on : 29/08/2017, 18:23:49
    Author     : allex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <div id="page-wrapper">
            
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">
                        Cursos 
                        <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                            <i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle btn-novo-curso" data-toggle="modal" data-target="#myModal"></i>
                        </c:if>
                    </h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">

                    <!-- .panel-heading -->
                    <div class="panel-body">
                        <div class="panel-group" id="accordion">
                            <c:forEach items="${listaCursos}" var="curso">
                                <div class="panel panel-default">

                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseCurso${curso.cursoId}" class="linkCursos" cursoId="${curso.cursoId}">${curso.cursoNome}</a>
                                            <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                <div style="float:right;">
                                                    <i style="font-size: 20px; color: #eea236;" idCurso="${curso.cursoId}" class="fa fa-edit edit-curso" data-toggle="modal" data-target="#myModal"></i>
                                                    <i style="font-size: 20px; color: #d9534f;" idCurso="${curso.cursoId}" class="glyphicon glyphicon-remove-circle delete-curso"></i>
                                                </div>
                                            </c:if>
                                        </h4>
                                    </div>

                                    <div id="collapseCurso${curso.cursoId}" class="panel-collapse collapse">
                                        <div class="panel-body">
                                            <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                <div>
                                                    <span class="nova-turma" cursoId="${curso.cursoId}" data-toggle="modal" data-target="#myModalTurma"><i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle"></i> Adicionar nova turma<span>
                                                </div>
                                                </br>
                                            </c:if>
                                            <div class="turmas">
                                                <!--second level accordion-->
                                                <c:forEach items="${curso.turmas}" var="turma">
                                                    <div class="panel-group" id="accordion${curso.cursoId}${turma.turmaId}${turma.semestreId}">
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading">
                                                                <h4 class="panel-title">
                                                                    <a data-toggle="collapse" data-parent="#accordion${curso.cursoId}${turma.turmaId}${turma.semestreId}" href="#collapse${curso.cursoId}${turma.turmaId}${turma.semestreId}">${turma.semestreNome}</a>
                                                                    <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                                        <div style="float:right;">
                                                                            <i style="font-size: 20px; color: #d9534f;" turmaId="${turma.turmaId}" class="glyphicon glyphicon-remove-circle remover-turma"></i>
                                                                        </div>
                                                                    </c:if>
                                                                </h4>
                                                            </div>

                                                            <div id="collapse${curso.cursoId}${turma.turmaId}${turma.semestreId}" class="panel-collapse collapse">
                                                                <div class="panel-body">

                                                                    <!--thidr level accordion-->
                                                                    <c:forEach items="${turma.turnos}" var="turno">
                                                                        <div class="panel-group" id="accordion${curso.cursoId}${turma.turmaId}-${turma.semestreId}-${turno.turnoId}">
                                                                            <div class="panel panel-default">
                                                                                <div class="panel-heading">
                                                                                    <h4 class="panel-title">
                                                                                        <a data-toggle="collapse" data-parent="#accordion${curso.cursoId}${turma.turmaId}-${turma.semestreId}-${turno.turnoId}" href="#collapse${curso.cursoId}${turma.turmaId}${turma.semestreId}${turno.turnoId}">${turno.turnoNome}</a>
                                                                                    </h4>
                                                                                </div>
                                                                                <div id="collapse${curso.cursoId}${turma.turmaId}${turma.semestreId}${turno.turnoId}" class="panel-collapse collapse">
                                                                                    <div class="panel-body">
                                                                                        <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                                                            <div>
                                                                                                <span class="addPeriodoEmTurma" turmaId="${turno.newTurmaId}" data-toggle="modal" data-target="#myAddPeriodo"><i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle"></i> Adicionar Período<span>
                                                                                            </div>
                                                                                            </br>
                                                                                        </c:if>
                                                                                        <!--4º level accordion-->
                                                                                            <c:forEach items="${turno.periodos}" var="periodo">
                                                                                                <div class="panel-group" id="accordion${curso.cursoId}${turma.turmaId}-${turma.semestreId}-${turno.turnoId}-${periodo.periodoId}">
                                                                                                    <div class="panel panel-default">
                                                                                                        <div class="panel-heading">
                                                                                                            <h4 class="panel-title">
                                                                                                                <a data-toggle="collapse" data-parent="#accordion${curso.cursoId}${turma.turmaId}-${turma.semestreId}-${turno.turnoId}-${periodo.periodoId}" href="#collapse${curso.cursoId}${turma.turmaId}${turma.semestreId}${turno.turnoId}${periodo.periodoId}">${periodo.periodoNome}</a>
                                                                                                                <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                                                                                    <div style="float:right;">
                                                                                                                        <i style="font-size: 20px; color: #d9534f;" turmaId="${turma.turmaId}" periodoId="${periodo.periodoId}" class="glyphicon glyphicon-remove-circle remover-periodo-turma"></i>
                                                                                                                    </div>
                                                                                                                </c:if>
                                                                                                            </h4>
                                                                                                        </div>
                                                                                                        <div id="collapse${curso.cursoId}${turma.turmaId}${turma.semestreId}${turno.turnoId}${periodo.periodoId}" class="panel-collapse collapse">
                                                                                                            <div class="panel-body">
                                                                                                                <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                                                                                    <div>
                                                                                                                        <span class="addDisciplinaEmPeriodo" cursoId="${curso.cursoId}" turmaId="${turma.turmaId}" periodoId="${periodo.periodoId}"  data-toggle="modal" data-target="#myAddDisciplina"><i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle"></i> Adicionar Disciplina<span>
                                                                                                                    </div>
                                                                                                                    </br>
                                                                                                                </c:if>
                                                                                                                <div class="list-group">
                                                                                                                    <c:forEach items="${periodo.disciplinas}" var="disciplina">
                                                                                                                        <a href="${pageContext.request.contextPath}/ProcessaDisciplina?function=disciplinaDetail&id=${disciplina.disciplinaId}" class="list-group-item">
                                                                                                                            ${disciplina.disciplinaNome}
                                                                                                                            <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                                                                                                <div style="float:right;">
                                                                                                                                    <i style="font-size: 20px; color: #d9534f;" turmaId="${turma.turmaId}" periodoId="${periodo.periodoId}" disciplinaId="${disciplina.disciplinaId}" class="glyphicon glyphicon-remove-circle remover-disciplina-periodo"></i>
                                                                                                                                </div>
                                                                                                                            </c:if>
                                                                                                                        </a>
                                                                                                                    </c:forEach>
                                                                                                                </div>
                                                                                                            </div>
                                                                                                        </div>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </c:forEach>
                                                                                        <!--end 4º level accordion-->
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </c:forEach>
                                                                    <!--end second level accordion-->

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                                <!--end second level accordion-->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>

                        </div>
                    </div>
                    <!-- .panel-body -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Novo Curso</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idCursoHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="nome">Nome: <span style='color:red'>*</span></label>
                                <input type="text" class="form-control" name="nome" id="nome">
                            </div>
                        </form>

                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-curso">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    
    
    <div class="modal fade" id="myModalTurma" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Nova Turma</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idTurmaCursoHide' value='0'></input>
                            <input type='text' class="hidden" id='idTurmaHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="nomeTurma">Nome: <span style='color:red'>*</span></label>
                                <input type="text" class="form-control" name="nomeTurma" id="nomeTurma">
                            </div>
                            <div class="form-group has-feedback">
                                <label for="divSelectSemestre">Semestre: <span style='color:red'>*</span></label>
                                <select class="form-control divSelectSemestre" name="divSelectSemestre"></select>
                            </div>
                            <div class="form-group has-feedback">
                                <label for="divSelectTurno">Turno: <span style='color:red'>*</span></label>
                                <select class="form-control divSelectTurno" name="divSelectTurno"></select>
                            </div>
                        </form>

                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-turma">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    
    
    <div class="modal fade" id="myAddPeriodo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Adicionar Período</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idPeriodoTurmaHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="divSelectPeriodo">Período: <span style='color:red'>*</span></label>
                                <select class="form-control divSelectPeriodo" name="divSelectPeriodo"></select>
                            </div>
                        </form>

                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-add-periodo">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    
    <div class="modal fade" id="myAddDisciplina" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Adicionar Disciplina</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idDisciplinaTurmaHide' value='0'></input>
                            <input type='text' class="hidden" id='idDisciplinaPeriodoHide' value='0'></input>
                            <input type='text' class="hidden" id='idDisciplinaCursoHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="divSelectDisciplina">Disciplina <span style='color:red'>*</span></label>
                                <select class="form-control divSelectDisciplina" name="divSelectDisciplina"></select>
                            </div>
                        </form>

                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-add-disciplina">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->

<script type="text/javascript">
    
    $(document).ready(function(){ 
        
        $('.linkCursos').on('click',function(){
            var cursoId = $(this).attr('cursoId');
            
            $.ajax({
                type: "POST",
                url: "ProcessaCurso",
                data:{
                    'function' : 'getTurmasByCurso',
                    "cursoId": cursoId
                },
                success: function (data) {
                    var disciplina = $.parseJSON(data);
//                    var option = "<option value='0'> </option>";
//                    for(var i = 0; i < disciplina.length; i++){
//                        option += "<option value='"+disciplina[i].id+"'>"+disciplina[i].nome+"</option>";
//                    }
//                    $(".divSelectDisciplina").html(option);
                }
            });
        });
        
        function getDisciplinas(turmaId, periodoId, cursoId){
            $.ajax({
                type: "POST",
                url: "ProcessaDisciplina",
                data:{
                    'function' : 'getToSelect',
                    "turmaId": turmaId,
                    "periodoId": periodoId,
                    "cursoId": cursoId
                },
                success: function (data) {
                    var disciplina = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < disciplina.length; i++){
                        option += "<option value='"+disciplina[i].id+"'>"+disciplina[i].nome+"</option>";
                    }
                    $(".divSelectDisciplina").html(option);
                }
            });
        }
        
        function getPeriodos(turmaId){
            $.ajax({
                type: "POST",
                url: "ProcessaPeriodo",
                data:{
                    'function' : 'getToSelect',
                    "turmaId": turmaId,
                },
                success: function (data) {
                    var periodo = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < periodo.length; i++){
                        option += "<option value='"+periodo[i].id+"'>"+periodo[i].nome+"</option>";
                    }
                    $(".divSelectPeriodo").html(option);
                }
            });
        }
        
        function getTurno(cursoId){
            var semestreId = $('.divSelectSemestre').val();
            $.ajax({
                type: "POST",
                url: "ProcessaTurno",
                data:{
                    'function' : 'getToSelect',
                    "cursoId": cursoId,
                    "semestreId": semestreId,
                },
                success: function (data) {
                    var turno = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < turno.length; i++){
                        option += "<option value='"+turno[i].id+"'>"+turno[i].nome+"</option>";
                    }
                    $(".divSelectTurno").html(option);
                }
            });
        }
        
        function getSemestre(cursoId){
            $.ajax({
                type: "POST",
                url: "ProcessaSemestre",
                data:{
                    'function' : 'getToSelect',
                    'cursoId'  : cursoId
                },
                success: function (data) {
                    var semestre = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < semestre.length; i++){
                        option += "<option value='"+semestre[i].id+"'>"+semestre[i].nome+"</option>";
                    }
                    $(".divSelectSemestre").html(option);
                }
            });
        }
        
        $('.remover-turma').on('click',function(){
            var turmaId = $(this).attr('turmaId');
            var r = confirm("Esta ação irá excluir todas os dados desta turma, Tem certeza que deseja excluir ?");
            if (r == true) {
                $.ajax({
                    type: "POST",
                    url: "ProcessaTurma",
                    data:{
                        'function'    : 'delete',
                        'turmaId'     : turmaId
                    },
                    success: function (data) {
                        location.reload();
                    }
                });
            }else{
                return false;
            }
        });
        
        $('.remover-periodo-turma').on('click',function(){
            var turmaId = $(this).attr('turmaId');
            var periodoId = $(this).attr('periodoId');
            
            var r = confirm("Esta ação irá excluir todas as disciplinas deste período, Tem certeza que deseja excluir ?");
            if (r == true) {
                $.ajax({
                    type: "POST",
                    url: "ProcessaPeriodo",
                    data:{
                        'function'    : 'removerDeTurma',
                        'periodoId'   : periodoId,
                        'turmaId'     : turmaId
                    },
                    success: function (data) {
                        location.reload();
                    }
                });
            }else{
                return false;
            }
        });
        
        $('.remover-disciplina-periodo').on('click',function(){
            var turmaId = $(this).attr('turmaId');
            var periodoId = $(this).attr('periodoId');
            var disciplinaId = $(this).attr('disciplinaId');
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                $.ajax({
                    type: "POST",
                    url: "ProcessaDisciplina",
                    data:{
                        'function'    : 'removerDePeriodo',
                        'periodoId'   : periodoId,
                        'disciplinaId': disciplinaId,
                        'turmaId'     : turmaId
                    },
                    success: function (data) {
                        location.reload();
                    }
                });
            }else{
                return false;
            }
        });
        
        $('.divSelectSemestre').on('change',function(){
            getTurno($('#idTurmaCursoHide').val());
        });
        
        $('.addDisciplinaEmPeriodo').on('click',function(){
            var turmaId = $(this).attr('turmaId');
            var periodoId = $(this).attr('periodoId');
            var cursoId = $(this).attr('cursoId');
            getDisciplinas(turmaId, periodoId,cursoId);
            $("#idDisciplinaTurmaHide").val(turmaId);
            $("#idDisciplinaPeriodoHide").val(periodoId);
            $("#idDisciplinaCursoHide").val(cursoId);
        });
        
        $('.addPeriodoEmTurma').on('click',function(){
            var turmaId = $(this).attr('turmaId');
            getPeriodos(turmaId);
            $("#idPeriodoTurmaHide").val(turmaId);
            
        });
        
        $('.nova-turma').on('click',function(){
            var cursoId = $(this).attr('cursoId');
            getSemestre(cursoId);
            $('#idTurmaCursoHide').val(cursoId);
        });
        
        $('.btn-add-disciplina').on('click',function(){
            var turmaId = $('#idDisciplinaTurmaHide').val();
            var periodoId = $('#idDisciplinaPeriodoHide').val();
            var disciplinaId = $(".divSelectDisciplina").val();
            if(periodoId > 0){
                $.ajax({
                    type: "POST",
                    url: "ProcessaDisciplina",
                    data:{
                        'function' : 'addDisciplinaEmPeriodo',
                        'turmaId' : turmaId,
                        'periodoId' : periodoId,
                        'disciplinaId' : disciplinaId,
                    },
                    success: function (data) {
                        $('#myModal').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }
        });
        
        $('.btn-add-periodo').on('click',function(){
            var turmaId = $('#idPeriodoTurmaHide').val();
            var periodoId = $('.divSelectPeriodo').val();
            
            if(periodoId > 0){
                $.ajax({
                    type: "POST",
                    url: "ProcessaPeriodo",
                    data:{
                        'function' : 'addPeriodoEmTurma',
                        'turmaId' : turmaId,
                        'periodoId' : periodoId,
                    },
                    success: function (data) {
                        $('#myModal').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }
        });
        
        $('.btn-save-turma').on('click',function(){
            var id = $('#idTurmaHide').val();
            var idCurso = $('#idTurmaCursoHide').val();
            var nome = $('#nomeTurma').val();
            var turnoId = $(".divSelectTurno").val();
            var semestreId = $(".divSelectSemestre").val();
            
            if((id && $.trim(nome)) || !id && $.trim(nome) != "" && turnoId > 0 && semestreId > 0 && idCurso > 0){
                $.ajax({
                    type: "POST",
                    url: "ProcessaTurma",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'idCurso' : idCurso,
                        'nome': nome,
                        'turnoId': turnoId,
                        'semestreId': semestreId,
                    },
                    success: function (data) {
                        $('#myModal').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }
            
        });
    
        $('.btn-save-curso').on('click', function(){
            //verifica se tem id escondido
            var id = $("#idCursoHide").val();
            var nome = $("#nome").val();
            
            if((id && $.trim(nome)) || !id && $.trim(nome) != ""){
                $.ajax({
                    type: "POST",
                    url: "ProcessaCurso",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'nome': nome
                    },
                    success: function (data) {
                        $('#myModal').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }

        });
        
        $('.edit-curso').on('click',function(){
            $('#myModal').find('.modal-title').html('Edição de Curso');
            
            var id = $(this).attr('idCurso');
            $.ajax({
                type: "POST",
                url: "ProcessaCurso",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var curso = $.parseJSON(data);
                    $("#idCursoHide").val(curso.id);
                    $("#nome").val(curso.nome);
                }
            });
        });
        
        $('.delete-curso').on('click',function(){
            var r = confirm("Esta ação irá excluir todas as turmas e disciplinas deste curso, Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idCurso');
                $.ajax({
                    type: "POST",
                    url: "ProcessaCurso",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('curso deletado com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
        });
    });
</script>
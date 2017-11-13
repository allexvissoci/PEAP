<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="wrapper">
    <jsp:include page="menu.jsp"/>
    <div id="page-wrapper">

        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">${prova.descricao}</h1>
                <input type="text" class="hidden idProva" value="${prova.id}">
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="row">
            <div class="col-lg-12">
                <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                    <c:if test="${podeEditar}">
                        <div class="div-add-questao">
                            <span class="newoptions nova-questao" idProva="${prova.id}" data-toggle="modal" data-target="#myModalQuestao"><i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle"></i> Nova Questão</span>
                        </div>
                        <hr>
                    </c:if>
                </c:if>
                <c:if test="${prova.observacao ne ''}">
                    <div class="div-observacao">
                        <h4>Observações:</h4>
                        <span>${prova.observacao}</span>
                    </div>
                    <hr>
                </c:if>
                <div class="div-questoes">
                    <c:set var="count" value="1" scope="page" />
                    <c:forEach items="${questoes}" var="questao">
                        <div class="div-questao" idQuestao="${questao.questaoId}" anulada="${questao.anulada}">
                            <span> 
                                ${count}) ${questao.enunciado} 
                                <c:if test="${questao.anulada}">
                                    <span style="color:red">--ANULADA--</span>
                                </c:if>
                                <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                                    <c:choose>
                                        <c:when test="${podeEditar}">
                                            <i style="font-size: 20px; color: #eea236;" idQuestao="${questao.questaoId}" class="fa fa-edit edit-questao" data-toggle="modal" data-target="#myModalQuestao"></i>
                                            <i style="font-size: 20px; color: #d9534f;" idQuestao="${questao.questaoId}" class="glyphicon glyphicon-remove-circle delete-questao"></i>
                                        </c:when>    
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${!questao.anulada}">
                                                    <i style="font-size: 20px; color: #d9534f;" idQuestao="${questao.questaoId}" anular="true" class="fa fa-thumbs-down anula-questao"></i>
                                                </c:when>    
                                                <c:otherwise>
                                                    <i style="font-size: 20px; color: #337ab7;" idQuestao="${questao.questaoId}" anular="false" class="fa fa-thumbs-up anula-questao"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </span>
                            <br>
                            <small>Nível Dificuldade: ${questao.nivelDificuldade} - Valor: (${questao.valor})</small>
                            <br><br>
                            <c:if test="${(sessionScope.tipoUsuario eq 'Administrador') || (sessionScope.tipoUsuario eq 'Professor')}">
                                <c:if test="${podeEditar}">
                                    <div class="div-add-alternativa">
                                        <span class="alternativasLink" idQuestao="${questao.questaoId}" data-toggle="modal" data-target="#myModalAlternativa"><i style="font-size: 20px;color: #337ab7;" class="fa fa-check-square-o "></i> Alternativas</span>
                                        <br><br>
                                    </div>
                                </c:if>
                            </c:if>
                            <c:forEach items="${questao.alternativas}" var="alternativa">
                                <span>
                                    <input type="radio" name="alternativa${questao.questaoId}" class="alternativa" alternativaId="${alternativa.alternativaId}"/> 
                                    <span>
                                        ${alternativa.alternativaDescricao}
                                    </span>
                                    <br><br>
                                </span>
                            </c:forEach>
                            <hr>
                            <c:set var="count" value="${count + 1}" scope="page"/>
                        </div>
                    </c:forEach>    
                </div>
                <c:if test="${resolvida ne true}">
                    <button type="button" class="btn btn-primary btn-save-prova">Salvar</button>    
                </c:if>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>
</div>

                
<!--MODAL PARA PROVA-->
<div class="modal fade" id="myModalQuestao" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Nova Prova</h4>
            </div>
            <div class="modal-body">                
                <form action='' method='post'>
                    <input type='text' class="hidden" id='idQuestaoHide' value='0'></input>
                    <input type='text' class="hidden" id='idProvaHide' value='${prova.id}'></input>
                    <div class="form-group has-feedback">
                        <label for="enunciado">Enunciado <span style='color:red'>*</span></label>
                        <textarea rows="10" class="form-control" name="enunciado" id="enunciado"></textarea>
                    </div>
                    <div class="form-group has-feedback">
                        <label for="valor">Valor <span style='color:red'>*</span></label>
                        <input type="text" class="form-control" name="valor" id="valor">
                    </div>
                    <div class="form-group has-feedback">
                        <label for="nivelDificuldade">Nivel Dificuldade <span style='color:red'>*</span></label>
                        <select class="form-control" id="nivelDificuldadeSelect"></select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary btn-save-questao">Salvar</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->


<!--MODAL PARA PROVA-->
<div class="modal fade" id="myModalAlternativa" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Nova Alternativa</h4>
            </div>
            <div class="modal-body">                
                <form action='' method='post'>
                    <input type='text' class="hidden" id='idQuestaoAlternativaHide' value='0'></input>
                    <input type='text' class="hidden" id='idAlternativaHide' value='0'></input>
                    <div class="simpleAlternativaForm">
                        <div class="alternativaFields">
                            <label class="alternativaIndice"></label>
                            <div class="form-group has-feedback">
                                <label for="descricao">Descricao <span style='color:red'>*</span></label>
                                <textarea rows="4" class="form-control descricao" name="descricao"></textarea>
                            </div>
                            <div class="form-group has-feedback">
                                <label for="correta">Correta </label>
                                <input type="radio" name="correta" class="correta">
                            </div>
                        </div>
                    </div>
                    <div class="novasAlternativas"></div>
                    <span class="nova-alternativa" idQuestao="${questao.questaoId}" ><i style="font-size: 20px;color: #337ab7;" class="fa fa-check-square-o "></i> Nova Alternativa</span>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary btn-save-alternativa">Salvar</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<a class="linkProvaDetail" href="${pageContext.request.contextPath}/ProcessaProva"></a>

<script type="text/javascript">
    
    $(document).ready(function(){
        
        $('.anula-questao').on('click',function(){
            var idQuestao = $(this).attr('idQuestao');
            var idProva = $('.idProva').val();
            var anular = $(this).attr("anular");
            
            $.ajax({
                type: "POST",
                url: "ProcessaQuestao",
                data:{
                    'function' : 'anula',
                    'idProva'  : idProva,
                    'idQuestao': idQuestao,
                    'anular': anular,
                },
                success: function (data) {
                    location.reload();
                }
            });
            
        });
        
        if(${resolvida}){
            ////desabilita todas os radios button
            $('.alternativa').each(function(){
                $(this).prop('disabled',true);
            });
            
            var idProva = $('.idProva').val();
            $('.div-questao').each(function(){
                var idQuestao = $(this).attr('idquestao');
                ////recupera resolucao da prova
                $.ajax({
                    type: "POST",
                    url: "ProcessaProva",
                    data:{
                        'function' : 'getResolucao',
                        'idProva'  : idProva,
                        'idQuestao': idQuestao,
                    },
                    success: function (data) {
                        var resolucao = $.parseJSON(data);
                        if(resolucao.alternativaSelecionada.id != resolucao.alternativaCorreta.id){
                            $('.alternativa[alternativaid='+resolucao.alternativaSelecionada.id+']','.div-questao[idquestao='+idQuestao+']').prop('checked', true).parent().css('background-color', 'red');
                        }else{
                            $('.alternativa[alternativaid='+resolucao.alternativaSelecionada.id+']','.div-questao[idquestao='+idQuestao+']').prop('checked', true);
                        }
                        $('.alternativa[alternativaid='+resolucao.alternativaCorreta.id+']','.div-questao[idquestao='+idQuestao+']').parent().css('background-color', 'green');
                    }
                });
            });
            
        }
        
        $('.btn-save-prova').on('click',function(){
            
            var arrayRespostas = [];
            $('.div-questoes .div-questao').each(function(){
                var idQuestao = $(this).attr('idQuestao');
                var idAlternativa = $('.alternativa:checked' , this).attr('alternativaid');
                var anulada = $(this).attr('anulada');

                arrayRespostas.push({
                   'idQuestao' : idQuestao,
                   'anulada': anulada,
                   'idAlternativa' : idAlternativa,
                });
                
            });
            
            var idProva = $('.idProva').val();
            //// chama ajax para corrigir respostas.
            $.ajax({
                type: "POST",
                url: "ProcessaProva",
                data:{
                    'function' : 'correcao',
                    'idProva'  : idProva,
                    'arrayRespostas' : JSON.stringify(arrayRespostas),
                },
                success: function (data) {
                    if(data){
                        var href =$('.linkProvaDetail').attr('href');
                        window.location.href = href+'?function=provaDetail&id='+idProva+'&resolvida='+true;
                    }else{
                        alert("Você já resolveu esta prova.");
                    }
                }
            });
            
        });
        
        function getNivelDificuldade(){
            $.ajax({
                type: "POST",
                url: "ProcessaQuestao",
                data:{
                    'function' : 'getNivelDificuldadeToSelect',
                },
                success: function (data) {
                    var nivel = $.parseJSON(data);
                    var option = "<option value='0'></option>";
                    for(var i = 0; i < nivel.length; i++){
                        option += "<option value='"+nivel[i].id+"'>"+nivel[i].descricao+"</option>";
                    }
                    $("#nivelDificuldadeSelect").html(option);
                }
            });
        }
        
        $('.nova-questao').on('click',function(){
            getNivelDificuldade();
        });
        
        $('.delete-questao').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idQuestao');
                $.ajax({
                    type: "POST",
                    url: "ProcessaQuestao",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('Questão deletada com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
        });
        
        $('.edit-questao').on('click',function(){
            $('#myModalQuestao').find('.modal-title').html('Edição de Questao');
            getNivelDificuldade();
            var id = $(this).attr('idQuestao');
            $.ajax({
                type: "POST",
                url: "ProcessaQuestao",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var questao = $.parseJSON(data);
                    $("#idQuestaoHide").val(questao.id);
                    $("#enunciado").val(questao.enunciado);
                    $("#valor").val(questao.valor);
                    setTimeout(function(){
                        $("#nivelDificuldadeSelect").val(questao.nivelDificuldade.id);
                    },20);
                }
            });
        });
        
        $('.btn-save-questao').on('click', function(){
            var id = $("#idQuestaoHide").val();
            var idProva = $("#idProvaHide").val();
            var nivelDificuldadeId = $("#nivelDificuldadeSelect").val();
            var enunciado = $('#enunciado').val();
            var valor = $('#valor').val();
            if((id && $.trim(enunciado) && $.trim(valor) && nivelDificuldadeId > 0 ) || (!id && $.trim(enunciado) && $.trim(valor) && nivelDificuldadeId > 0)){
                $.ajax({
                    type: "POST",
                    url: "ProcessaQuestao",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'idProva' : idProva,
                        'nivelDificuldadeId': nivelDificuldadeId,
                        'enunciado': enunciado,
                        'valor': valor,
                    },
                    success: function (data) {
                        $('#myModalQuestao').modal('hide');
                        location.reload();
                    }
                });
            }else{
                alert("Preencha os campos obrigatórios");
            }
        });
        
        
        $('.alternativasLink').on('click',function(){
            $('.novasAlternativas').html("");
            var idQuestao = $(this).attr("idQuestao");
            $("#idQuestaoAlternativaHide").val(idQuestao);
            ////recupera alternativas da questão e coloca no modal para edição se tiver, se não, mostra modal simples
            $.ajax({
                type: "POST",
                url: "ProcessaAlternativa",
                data:{
                    'function' : 'getDadosByQuestao',
                    'idQuestao' : idQuestao,
                },
                success: function (data) {
                    console.log(data);
                    var alternativas = $.parseJSON(data);
                    var simpleAlternativaForm = $('.simpleAlternativaForm').html();
                    for(var i = 0; i < alternativas.length; i++){
                        var simpleAlternativaFormAux = simpleAlternativaForm;
                        if(i == 0){
                            $('.simpleAlternativaForm .descricao').html(alternativas[i]['descricao']);
                            if(alternativas[i]['correta']){
                                $('.simpleAlternativaForm .correta').prop('checked',true);
                            }else{
                                $('.simpleAlternativaForm .correta').prop('checked',false);
                            }
                        }else{
                            var html = '<div class="alternativaFields">\n\
                                            <label class="alternativaIndice"></label>\n\
                                            <div class="form-group has-feedback">\n\
                                                <label for="descricao">Descricao <span style="color:red">*</span></label>\n\
                                                <textarea rows="4" class="form-control descricao" name="descricao">'+alternativas[i]['descricao']+'</textarea>\n\
                                            </div>\n\
                                            <div class="form-group has-feedback">\n\
                                                <label for="correta">Correta </label>';
                                                if(alternativas[i]['correta']){ 
                                                    html += ' <input type="radio" checked name="correta" class="correta">';
                                                }else{
                                                    html += ' <input type="radio" name="correta" class="correta">';
                                                }
                                            html += '</div>\n\
                                        </div>';
                            $('.novasAlternativas').append($(html));
                            var counter = 1;
                            $('.alternativaFields').each(function(){
                                var icon = "";
                                if(counter > 1){
                                    icon = "<i style='font-size: 20px; color: #d9534f;' class='glyphicon glyphicon-remove-circle remove-alternativa'></i>";
                                }
                                $('.alternativaIndice',$(this)).html("Alternativa "+counter+": "+icon);
                                counter++;
                            });
                            removeAlternativa();
                        }
                    }
                }
            });
        });
        
        $('.nova-alternativa').on('click',function(){
            //// adiciona elementos no formulário
            var html = '<div class="alternativaFields">\n\
                            <label class="alternativaIndice"></label>\n\
                            <div class="form-group has-feedback">\n\
                                <label for="descricao">Descricao <span style="color:red">*</span></label>\n\
                                <textarea rows="4" class="form-control descricao" name="descricao"></textarea>\n\
                            </div>\n\
                            <div class="form-group has-feedback">\n\
                                <label for="correta">Correta </label>\n\
                                    <input type="radio" name="correta" class="correta">\n\
                            </div>\n\
                        </div>';
            $('.novasAlternativas').append($(html));
            var counter = 1;
            $('.alternativaFields').each(function(){
                var icon = "";
                if(counter > 1){
                    icon = "<i style='font-size: 20px; color: #d9534f;' class='glyphicon glyphicon-remove-circle remove-alternativa'></i>";
                }
                $('.alternativaIndice',$(this)).html("Alternativa "+counter+": "+icon);
                counter++;
            });
            removeAlternativa();
            
        });
        
        removeAlternativa();
        
        function removeAlternativa(){
            $('.remove-alternativa').on('click',function(){
                if($('.alternativaFields').length > 1){
                    $(this).parent().parent().detach();
                    if($('.alternativaFields').length == 1){
                        $('.alternativaFields').children().children('i').detach();
                    }
                }else{
                    $('.alternativaFields').children().children('i').detach();
                }
            });
        }
        
        
        $('.btn-save-alternativa').on('click', function(){
            
            var counter = 1;
            var alternativasArray = [];
            var corretaBool = false;
            var descricaoVazia = false;
            $(".descricao").each(function(){
                
                if($(this).parent().next().children("input").is(":checked")){
                    corretaBool = true;
                }
                
                if($(this).val() == ""){
                    descricaoVazia = true;
                }
                
                alternativasArray.push({
                    'descricao': $(this).val(), 
                    'correta' : $(this).parent().next().children("input").is(":checked")
                });
                counter++;
                
            });
            
            if(!descricaoVazia){
                if(corretaBool){
                    var idQuestao = $("#idQuestaoAlternativaHide").val();
                    $.ajax({
                        type: "POST",
                        url: "ProcessaAlternativa",
                        data:{
                            'function' : 'insert',
                            'idQuestao' : idQuestao,
                            'alternativasArray': JSON.stringify(alternativasArray),
                        },
                        success: function (data) {
                            $('#myModalAlternativa').modal('hide');
                            location.reload();
                        }
                    });
                }else{
                    alert("Pelo menos uma das alternativas deve ser marcadas como correta");
                }
            }else{
                alert("Preencha os campos obrigatórios");
            }
            
        });
    });
    
</script>
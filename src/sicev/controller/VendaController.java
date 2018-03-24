/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import sicev.DAO.DaoClientes;
import sicev.DAO.DaoProdutos;
import sicev.DAO.DaoVenda;
import sicev.DAO.DaoVendaProduto;
import sicev.model.ModelClientes;
import sicev.model.ModelProdutos;
import sicev.model.ModelVenda;
import sicev.model.ModelVendaProduto;
import sicev.model.ProdutosDaVenda;
import sicev.view.SICEV;

/**
 *
 * @author Moacir
 */
public class VendaController implements Initializable{

    @FXML private GridPane paneCampos;
    @FXML private VBox tableContainer;
    @FXML private TableView<ModelVenda> tabelaVendas;
    @FXML private ToggleButton btnNovo;
    @FXML private ToggleButton btnEditar;
    @FXML private TextField edtPesquisar;
    @FXML private Button btnRemoveFiltro;
    @FXML private Button btnExcluir;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    
    @FXML private TextField edtCodVenda;
    @FXML private TextField edtCodCliente;
    @FXML private TextField edtCodProduto;
    @FXML private TextField edtValorLiq;
    @FXML private TextField edtDesconto;
    @FXML private TextField edtValorTotal;
    @FXML private TextField edtQtde;
    @FXML private ComboBox cmbCliente;
    @FXML private ComboBox cmbProduto;
    @FXML private Button btnAddProduto;
    @FXML private TableView<ProdutosDaVenda> tabelaProdsVenda;
    
    private final DaoVenda daoVenda = new DaoVenda();
    private final ModelVenda modelVenda = new ModelVenda();
    
    private final DaoClientes daoCliente = new DaoClientes();
    private final ModelClientes modelCliente = new ModelClientes();
    
    private final DaoProdutos daoProduto = new DaoProdutos();
    private final ModelProdutos modelProduto = new ModelProdutos();
    
    private final BooleanProperty tableSelectionIsNull = new SimpleBooleanProperty();
    private final BooleanProperty btnNovoSelected = new SimpleBooleanProperty();
    private final BooleanProperty btnEditarSelected = new SimpleBooleanProperty();
    
    private ObservableList<ModelVenda> listaVendas;
    private FilteredList<ModelVenda> listaVendasFiltrada;
    
    private ObservableList<ModelClientes> listaClientes;
    private FilteredList<ModelClientes> listaClientesFiltrada;
    
    private ObservableList<ModelProdutos> listaTodosProdutos;
    private FilteredList<ModelProdutos> listaTodosProdutosFiltrada;
    
    private ObservableList<ProdutosDaVenda> listaTabelaProdsVenda;
    
    private SICEV application;
    
    /**
     * Expressão lambda que implementa o bind dos campos com a linha selecionada
     * da tabela
     */
    final ChangeListener<ModelVenda> TableListener = 
        (ObservableValue<? extends ModelVenda> observable, 
                ModelVenda oldValue, ModelVenda newValue) -> {
            
            setModelVendaBinds(newValue);
        };
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        carregarVendas();
        carregarCmbBoxClientes();
        carregarCmbBoxProdutos();
        
        definirBindsDeControle();
        definirRegraEstadoBotoes();
        definirRegraEstadoContainers();
        
        definirBindsCampos();
        definirRegraAutoPreenchimentoCamposCliente();
        definirRegraAutoPreenchimentoCamposProduto();
        
        definirMecanismoPesquisaVendas();
        
        carregarProdutosDaVenda();
        
        setStatusTelaExibir();
    }
    
    public void setApp(SICEV application) {
        this.application = application;
    }
    
    private void definirBindsDeControle() {
        tableSelectionIsNull.bind( tabelaVendas.getSelectionModel().selectedItemProperty().isNull() );
        btnNovoSelected.bind( btnNovo.selectedProperty() );
        btnEditarSelected.bind( btnEditar.selectedProperty() );
    }

    private void definirRegraEstadoBotoes() {
        btnNovo.disableProperty().bind(btnEditarSelected);
        btnEditar.disableProperty().bind(tableSelectionIsNull.or(btnNovoSelected));
        btnExcluir.disableProperty().bind(tableSelectionIsNull.
                or(btnNovoSelected).or(btnEditarSelected));
        btnCancelar.visibleProperty().bind( btnNovoSelected.or(btnEditarSelected) );
        btnSalvar.visibleProperty().bind(btnNovoSelected.or(btnEditarSelected) );
    }

    private void definirRegraEstadoContainers() {
        paneCampos.disableProperty().bind(
                ( btnNovoSelected.not() ).and(
                ( btnEditarSelected.not() )));
        tableContainer.disableProperty().bind(btnNovoSelected.or( btnEditarSelected ));
        
        tableSelectionIsNull.addListener( //Limpa os campos quando nenhuma linha 
                                          //da tabela estiver selecionada  
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        setModelVendaBinds(new ModelVenda());
                    }
                });
    }

    private void definirBindsCampos() {
        edtCodVenda.textProperty().bindBidirectional(
                modelVenda.idVendaProperty(), new NumberStringConverter());
        
        edtCodCliente.textProperty().bindBidirectional(
                modelVenda.idClienteProperty(), new NumberStringConverter());
        
        edtValorLiq.textProperty().bindBidirectional(
                modelVenda.valorProperty(), new NumberStringConverter());
        
        edtDesconto.textProperty().bindBidirectional(
                modelVenda.descontoProperty(), new NumberStringConverter());
        
        edtValorTotal.textProperty().bindBidirectional(
                modelVenda.valorTotalProperty(), new NumberStringConverter());
        
        edtCodProduto.textProperty().bindBidirectional(
                modelProduto.idProdutoProperty(), new NumberStringConverter());
        
        tabelaVendas.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
    }

    private void definirRegraAutoPreenchimentoCamposCliente() {
        modelVenda.idClienteProperty().addListener(
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                
                //Cria uma lista com apenas o cliente com o mesmo ID de NewValue
                FilteredList<ModelClientes> tempListCliente = new FilteredList<>(listaClientes);
                tempListCliente.setPredicate(cliente -> {
                    // If filter text is empty, display all produtos.
                    if (newValue == null) {
                        return false;
                    }

                    return newValue.equals(cliente.getIdCliente()); 
                });
                
                if (!tempListCliente.isEmpty()) {
                    cmbCliente.getSelectionModel().select(tempListCliente.get(0));
                }
                else {
                    cmbCliente.getSelectionModel().clearSelection();
                    cmbCliente.setPromptText("A venda será salva sem cliente");
                }
            });
        
        cmbCliente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    int index = listaClientes.indexOf(newValue);
                    
                    if ( index != -1 ) {
                        edtCodCliente.setText( String.valueOf(listaClientes.get(index).getIdCliente()) );
                    }
                    else {
                        //edtCodCliente.clear();
                    }
                });
    }
    
    private void definirRegraAutoPreenchimentoCamposProduto() {
        modelProduto.idProdutoProperty().addListener(
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                
                //Cria uma lista com apenas o cliente com o mesmo ID de NewValue
                FilteredList<ModelProdutos> tempListProds = new FilteredList<>(listaTodosProdutos);
                tempListProds.setPredicate(prod -> {
                    // If filter text is empty, display all produtos.
                    if (newValue == null) {
                        return false;
                    }

                    return newValue.equals(prod.getIdProduto()); 
                });
                
                if (!tempListProds.isEmpty()) {
                    cmbProduto.getSelectionModel().select(tempListProds.get(0));
                }
                else {
                    cmbProduto.getSelectionModel().clearSelection(); 
                    cmbProduto.setPromptText("Não existe produto com esse código");
                }
            });
        
        cmbProduto.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    int index = listaTodosProdutos.indexOf(newValue);
                    
                    if ( index != -1 ) {
                        edtCodProduto.setText( String.valueOf(listaTodosProdutos.get(index).getIdProduto()) );
                    }
                    else {
                        //edtCodCliente.clear();
                    }
                });
    }

    private void definirMecanismoPesquisaVendas() {
        edtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaVendasFiltrada.setPredicate(venda -> {
                
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //TODO : Filtro da tabela de vendas
                return true;
            });
        });
    }
    
    public void carregarVendas() {
        listaVendas = FXCollections.observableList(getAllvendas());
        listaVendasFiltrada = new FilteredList<>(listaVendas, p -> true);
        SortedList<ModelVenda> sortedData = new SortedList<>(listaVendasFiltrada);
        sortedData.comparatorProperty().bind(tabelaVendas.comparatorProperty());    
        
        //Remove o changeListener da tabela enquanto atualiza seus items
        tabelaVendas.getSelectionModel().selectedItemProperty().
                removeListener(TableListener);
        
        tabelaVendas.setItems(sortedData);
        
        tabelaVendas.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
    }
    
    private void carregarProdutosDaVenda() {
        listaTabelaProdsVenda = FXCollections.observableList(new ArrayList<>());
        tabelaProdsVenda.setItems(listaTabelaProdsVenda);
    }
    
    public void carregarCmbBoxClientes() {
        listaClientes = FXCollections.observableList(
                daoCliente.getAllClientesDao());
        cmbCliente.setItems(listaClientes);
    }
    
    public void carregarCmbBoxProdutos() {
        listaTodosProdutos = FXCollections.observableList(
                daoProduto.getAllProdutosDao());
        cmbProduto.setItems(listaTodosProdutos);
    }
    
    private void setModelVendaBinds (ModelVenda currentVenda) {
        if ( currentVenda != null ) {
            modelVenda.idVendaProperty().bind(currentVenda.idVendaProperty());
            modelVenda.idClienteProperty().bind(currentVenda.idClienteProperty());
            modelVenda.dataVendaProperty().bind(currentVenda.dataVendaProperty());
            modelVenda.valorProperty().bind(currentVenda.valorProperty());
            modelVenda.valorTotalProperty().bind(currentVenda.valorTotalProperty());
            modelVenda.descontoProperty().bind(currentVenda.descontoProperty());
        } 
        else {
            unsetModelVendaBinds();
        }
    }
    
    private void unsetModelVendaBinds() {
        modelVenda.idVendaProperty().unbind();
        modelVenda.idClienteProperty().unbind();
        modelVenda.dataVendaProperty().unbind();
        modelVenda.valorTotalProperty().unbind();
        modelVenda.valorProperty().unbind();
        modelVenda.descontoProperty().unbind();
    }
    
    /**
     * <p>seleciona a primeira linha na tabela</p>
     */
    private void setStatusTelaExibir () {
        setStatusTelaExibir(-1);
    }
    
    /**
     * <p>para selecionar a última linha passe -2 como parâmetro</p>
     * <p>para selecionar a primeira linha passe -1 como parâmetro</p>
     * @param tableRowIndex 
     */
    private void setStatusTelaExibir (int tableRowIndex) {
        btnNovo.setSelected(false);
        btnEditar.setSelected(false);
        
        selecionarLinhaTabela(tableRowIndex);
        
        tabelaVendas.requestFocus();
        
        setBindModelVendaLinhaAtual();

        //Devolve os eventos padrões dos botões Editar e Novo
        btnNovo.setOnAction((e) -> {handleBtnNovoAction(e);});
        btnEditar.setOnAction((e) -> {handleBtEditarAction(e);});
    }
    
    private void selecionarLinhaTabela(int tableRowIndex) {
        switch (tableRowIndex) {
            case -2:
                tabelaVendas.getSelectionModel().selectLast();
                break;
            case -1:
                tabelaVendas.getSelectionModel().selectFirst();
                break;
            default:
                tabelaVendas.getSelectionModel().select(tableRowIndex);
                break;
        }
    }

    private void setBindModelVendaLinhaAtual() {
        setModelVendaBinds(tabelaVendas.getSelectionModel().getSelectedItem());
    }
    
    private void cadastrarNovaVenda() {
        modelVenda.setDataVenda(LocalDate.now());
        
        int idNovaVenda = salvarVenda(modelVenda);
        if ( idNovaVenda > 0 ) {
            modelVenda.setIdVenda(idNovaVenda);
            salvarProdutosDaVenda();
            showAlert(Alert.AlertType.INFORMATION, "Venda cadastrada com sucesso!");
            carregarVendas();
        }   
        else {
            showAlert(Alert.AlertType.WARNING, "Não foi possível cadastrar a venda");
        }
        
        //TODO : Selecionar a linha certa depois de alterar uma venda
        setStatusTelaExibir(-2);
    }
    
    private void salvarProdutosDaVenda() {
        DaoVendaProduto.salvaProdutosDaVendaDao(getListaProdutosVenda());
    }
    
    private ArrayList<ModelVendaProduto> getListaProdutosVenda() {
        ArrayList<ModelVendaProduto> listaProdutos = new ArrayList<>();
        
        listaTabelaProdsVenda.forEach((produtoDaLista) -> {
            ModelVendaProduto novoProduto = new ModelVendaProduto();
            
            novoProduto.setIdVenda(modelVenda.getIdVenda());
            novoProduto.setIdProduto(produtoDaLista.getIdProduto());
            novoProduto.setProQtde(produtoDaLista.getQuantidade());
            novoProduto.setProValor(produtoDaLista.getProValor());
            
            listaProdutos.add(novoProduto);
        });
        
        return listaProdutos;
    }
    
    private void salvarAlteracaoVenda() {
        if ( atualizarVenda(modelVenda) ) {
                showAlert(Alert.AlertType.INFORMATION, "Alterações salvas com sucesso!");
                carregarVendas();
            }   
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível salvar as alterações");
            }

            setStatusTelaExibir(-2);
            setModelVendaBinds(tabelaVendas.getSelectionModel().getSelectedItem() );
    }
    
    private void darBaixaEstoque() {
        daoProduto.SalvarAlteracaoEstoqueDao(getListaProdutosVenda());
    }
    
    /* ====================== Métodos da classe Dao ========================= */
    private int salvarVenda (ModelVenda pModelUsuario) {
        return  this.daoVenda.SalvarVendaDao(pModelUsuario);
    }
    
    private boolean excluirVenda (int idUsuario) {
        return this.daoVenda.excluirVendaDao(idUsuario);
    }
    
    private boolean atualizarVenda (ModelVenda pModelUsuario) {
        return this.daoVenda.atualizarVendaDao(pModelUsuario);
    }
    
    private ModelVenda getVendaById (int idUsuario) {
        return this.daoVenda.getVendaById(idUsuario);
    }
    
    private ArrayList<ModelVenda> getAllvendas() {
        return this.daoVenda.getAllVendasDao();
    }
    /* ====================================================================== */
    
    /* ==================== Eventos da Interface ============================ */
    @FXML
    private void handleBtnNovoAction (ActionEvent event) {        
        
        limparCampos();
        
        //Necessário para que seja possível alterar os campos
        unsetModelVendaBinds();
        
        //Comfiguração dos componentes da tela
        edtCodCliente.requestFocus();
        btnNovo.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    private void limparCampos() {
        setModelVendaBinds(new ModelVenda());
    }
    
    @FXML
    private void handleBtEditarAction (ActionEvent event) {
        //Retira o bind do modelUsuario para que seja possível a alteração
        unsetModelVendaBinds();
        
        //Comfiguração dos componentes da tela
        edtCodCliente.requestFocus();
        edtCodCliente.selectEnd();
        btnEditar.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtnExcluirAction (ActionEvent event) {
        
        if (confirmarExclusao()) {
            
            int newIndex = 0;
            
            if ( excluirVenda(tabelaVendas.getSelectionModel().
                    getSelectedItem().getIdVenda()) ) {
                
                newIndex = getPreviousOrNextRowIndex();
                carregarVendas();
                
                showAlert(Alert.AlertType.INFORMATION, "Venda excuída com sucesso!");
            }
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível excluir a venda!");
            }
            
            //Deixa a tela no mode de exibição
            setStatusTelaExibir(newIndex);
        }
    }
    
    private int getPreviousOrNextRowIndex() {
        if ( tabelaVendas.getSelectionModel().isSelected(0) ) {
            tabelaVendas.getSelectionModel().selectNext();
        }
        else {
            tabelaVendas.getSelectionModel().selectPrevious();
        }
        return tabelaVendas.getSelectionModel().getSelectedIndex();
    }
    
    @FXML
    private void handleBtnSalvarAction (ActionEvent event) {
        
        if ( btnNovoSelected.get() ) {
            cadastrarNovaVenda();
            darBaixaEstoque();
        }
        else if ( btnEditarSelected.get() ) {
            salvarAlteracaoVenda();
        }
    }
    
    @FXML
    private void handleBtnCancelarAction (ActionEvent event) {
        setStatusTelaExibir( tabelaVendas.getSelectionModel().getSelectedIndex() );
        setBindModelVendaLinhaAtual();
    }
    
    @FXML
    private void handleBtnRemoveFiltro (ActionEvent event) {
        edtPesquisar.clear();
        edtPesquisar.requestFocus();
    }
    
    @FXML
    private void handleBtnAddProduto (ActionEvent event) {
        
        int idProduto = Integer.valueOf(edtCodProduto.getText());
        int quantidade = Integer.valueOf(edtQtde.getText());
        
        ProdutosDaVenda produtoDaVenda = 
                new ProdutosDaVenda(daoProduto.getProdutoByIdDao(idProduto));
        produtoDaVenda.setQuantidade(quantidade);
        produtoDaVenda.setValorTotal(quantidade * produtoDaVenda.getProValor());

        listaTabelaProdsVenda.add(produtoDaVenda);
    }
    
    /* ====================================================================== */
    
     public void showAlert(Alert.AlertType tipo, String texto) {
        Alert alert = new Alert(tipo);
        alert.setTitle("SICEV");
        alert.setHeaderText("Vendas");
        alert.setContentText(texto);
        
        alert.show();
    }
    
    private boolean confirmarExclusao() {
        return confirmAlert("Você está certo de excluir essa venda?") == 
                            ButtonBar.ButtonData.YES ;
    } 
     
    public ButtonBar.ButtonData confirmAlert(String texto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SICEV");
        alert.setHeaderText("vendas");
        alert.setContentText(texto);
        alert.getButtonTypes().setAll( 
                new ButtonType("Sim", ButtonBar.ButtonData.YES), 
                new ButtonType("Não", ButtonBar.ButtonData.NO) );
        
        return alert.showAndWait().get().getButtonData();
    }
}

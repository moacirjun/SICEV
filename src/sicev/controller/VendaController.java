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
        carregarClientes();
        carregarProdutos();
        
        tableSelectionIsNull.bind( tabelaVendas.getSelectionModel().selectedItemProperty().isNull() );
        btnNovoSelected.bind( btnNovo.selectedProperty() );
        btnEditarSelected.bind( btnEditar.selectedProperty() );
        
        //Regra dos estados dos botões
        btnNovo.disableProperty().bind(btnEditarSelected);
        btnEditar.disableProperty().bind(tableSelectionIsNull.or(btnNovoSelected));
        btnExcluir.disableProperty().bind(tableSelectionIsNull.
                or(btnNovoSelected).or(btnEditarSelected));
        
        btnCancelar.visibleProperty().bind( btnNovoSelected.or(btnEditarSelected) );
        btnSalvar.visibleProperty().bind(btnNovoSelected.or(btnEditarSelected) );
        
        //Regra dos estados dos containers da tela
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
        
        
        //Cria um bind bidirecional entre os campos e o objeto "modelVenda"
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
        
        //Bind automático dos campos ao selecionar uma linha na tabela
        tabelaVendas.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
        
        //Set the filter Predicate whenever the filter changes.
        edtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaVendasFiltrada.setPredicate(venda -> {
                // If filter text is empty, display all produtos.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //TODO : Filtro da tabela de vendas
                return true;
            });
        });
        
        listaTabelaProdsVenda = FXCollections.observableList( new ArrayList<>() );
        tabelaProdsVenda.setItems(listaTabelaProdsVenda);
        
        //Configura a tela
        setStatusTelaExibir();
    }
    
    public void setApp(SICEV application) {
        this.application = application;
    }
    
    public void carregarVendas() {
        listaVendas = FXCollections.observableList(getAllvendas());
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        listaVendasFiltrada = new FilteredList<>(listaVendas, p -> true);

        // 2. Wrap the FilteredList in a SortedList. 
        SortedList<ModelVenda> sortedData = new SortedList<>(listaVendasFiltrada);

        // 3. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tabelaVendas.comparatorProperty());    
        
        //Remove o changeListener da tabela enquanto atualiza seus items
        tabelaVendas.getSelectionModel().selectedItemProperty().
                removeListener(TableListener);
        
        // 4. Add sorted (and filtered) data to the table.
        tabelaVendas.setItems(sortedData);
        
        tabelaVendas.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
    }
    
    public void carregarClientes() {
        listaClientes = FXCollections.observableList(daoCliente.getAllClientesDao());
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        listaClientesFiltrada = new FilteredList<>(listaClientes, p -> true);

        // 2. Wrap the FilteredList in a SortedList. 
        SortedList<ModelClientes> sortedData = new SortedList<>(listaClientesFiltrada);

        // 3. Bind the SortedList comparator to the TableView comparator.
//        sortedData.comparatorProperty().bind(cmbCliente.comparatorProperty());    
        
        // 4. Add sorted (and filtered) data to the table.
        cmbCliente.setItems(sortedData);
    }
    
    public void carregarProdutos() {
        listaTodosProdutos = FXCollections.observableList(daoProduto.getAllProdutosDao());
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        listaTodosProdutosFiltrada = new FilteredList<>(listaTodosProdutos, p -> true);

        // 2. Wrap the FilteredList in a SortedList. 
        SortedList<ModelProdutos> sortedData = new SortedList<>(listaTodosProdutosFiltrada);
        
        // 3. Bind the SortedList comparator to the TableView comparator.
//        sortedData.comparatorProperty().bind(tabelaVendas.comparatorProperty());    
        
        // 4. Add sorted (and filtered) data to the table.
        cmbProduto.setItems(sortedData);
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
            modelVenda.idVendaProperty().unbind();
            modelVenda.idClienteProperty().unbind();
            modelVenda.dataVendaProperty().unbind();
            modelVenda.valorTotalProperty().unbind();
            modelVenda.valorProperty().unbind();
            modelVenda.descontoProperty().unbind();
            
        }
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
        
        tabelaVendas.requestFocus();
        
        /**
         * faz um bind com a linha atual da tabela, caso o evento ChangeLister
         * da tabela não seja invocado
         */        
        setModelVendaBinds( tabelaVendas.getSelectionModel().getSelectedItem() );

        //Devolve os eventos padrões dos botões Editar e Novo
        btnNovo.setOnAction((e) -> {handleBtnNovoAction(e);});
        btnEditar.setOnAction((e) -> {handleBtEditarAction(e);});
    }
    
    private void cadastrarNovaVenda() {
        modelVenda.setDataVenda(LocalDate.now());
        
        if ( salvarVenda(modelVenda) > 0 ) {
            salvarProdutosDaVenda();
            showAlert(Alert.AlertType.INFORMATION, "Venda cadastrada com sucesso!");
            carregarVendas();
        }   
        else {
            showAlert(Alert.AlertType.WARNING, "Não foi possível cadastrar a venda");
        }
        
        setStatusTelaExibir(-2);
        setModelVendaBinds(tabelaVendas.getSelectionModel().getSelectedItem());
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
        
        //passa um objeto ModelUsuario vazio para limpar os campos
        setModelVendaBinds( new ModelVenda() );
        
        //Retira o bind do modelUsuario com esse ModelUSuario vazio passado anteriormente
        setModelVendaBinds(null);
        
        //Comfiguração dos componentes da tela
        edtCodCliente.requestFocus();
        
        //No próximo click o botao executa a ação "Cancelar"
        btnNovo.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtEditarAction (ActionEvent event) {
        //Retira o bind do modelUsuario para que seja possível a alteração
        setModelVendaBinds(null);
        
        edtCodCliente.requestFocus();
        edtCodCliente.selectEnd();
        
        //No próximo click o botao executa a ação "Cancelar"
        btnEditar.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtnExcluirAction (ActionEvent event) {
        
        if (confirmAlert("Confirmar a exclusão da venda?") == ButtonBar.ButtonData.YES) {
            
            int newIndex = 0;
            
            if ( excluirVenda(tabelaVendas.getSelectionModel().
                    getSelectedItem().getIdVenda()) ) {
                
                //Guarda o index da linha anterior ou posterior à linha excluída
                int curIndex = tabelaVendas.getSelectionModel().getSelectedIndex();
                if ( curIndex ==  0) {
                    tabelaVendas.getSelectionModel().selectNext();
                }
                else {
                    tabelaVendas.getSelectionModel().selectPrevious();
                }
                newIndex = tabelaVendas.getSelectionModel().getSelectedIndex();
                
                //Recarrega os produtos do banco
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
        //Retorna para p mode de exibição, e no produto selecionado na tabela
        setStatusTelaExibir( tabelaVendas.getSelectionModel().getSelectedIndex() );
        setModelVendaBinds(tabelaVendas.getSelectionModel().getSelectedItem() );
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

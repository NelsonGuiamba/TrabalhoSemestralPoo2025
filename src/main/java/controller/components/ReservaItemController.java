package controller.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReservaItemController {

    public Label lbMesaNr;
    public Label lbDuracao;
    public Label lbMesaNome;
    public Label lbPessoas;
    public Label lbData;
    public Button btnCancelar;

    public void setMesa(String mesaNr){
        lbMesaNr.setText(mesaNr);
        lbMesaNome.setText("Mesa "+mesaNr);
    }

    public void setDuracao(String horaInicial, String duracao){
        lbDuracao.setText(horaInicial+" • "+duracao);
    }

    public void setPessoas(String pessoas){
        lbPessoas.setText("Pessoas "+pessoas);
    }

    public Button getBtnCancelar() {
        return btnCancelar;
    }

    public void setData(String data){
        lbData.setText(data);
    }
}

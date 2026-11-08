package cl.duoc.viviendas.dao;

import cl.duoc.viviendas.model.Casa;
import cl.duoc.viviendas.model.Departamento;
import cl.duoc.viviendas.model.Vivienda;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViviendaDAO {

    private final Gson gson = new Gson();

    private static final String CASAS_FILE = "casas.json";
    private static final String DEP_FILE = "departamentos.json";

    public void guardar(List<Vivienda> viviendas) {

        List<Casa> casas = new ArrayList<>();
        List<Departamento> departamentos = new ArrayList<>();

        for (Vivienda v : viviendas) {

            if (v instanceof Casa) {
                casas.add((Casa) v);
            }

            if (v instanceof Departamento) {
                departamentos.add((Departamento) v);
            }
        }

        try {

            FileWriter fwCasa = new FileWriter(CASAS_FILE);
            gson.toJson(casas, fwCasa);
            fwCasa.close();

            FileWriter fwDepto = new FileWriter(DEP_FILE);
            gson.toJson(departamentos, fwDepto);
            fwDepto.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<Vivienda> cargarTodo() {

        List<Vivienda> lista = new ArrayList<>();

        try {

            Type tipoCasa =
                    new TypeToken<List<Casa>>(){}.getType();

            List<Casa> casas =
                    gson.fromJson(
                            new FileReader(CASAS_FILE),
                            tipoCasa
                    );

            if(casas != null){
                lista.addAll(casas);
            }

        } catch(Exception ignored){}

        try {

            Type tipoDepto =
                    new TypeToken<List<Departamento>>(){}.getType();

            List<Departamento> deptos =
                    gson.fromJson(
                            new FileReader(DEP_FILE),
                            tipoDepto
                    );

            if(deptos != null){
                lista.addAll(deptos);
            }

        } catch(Exception ignored){}

        return lista;
    }
}
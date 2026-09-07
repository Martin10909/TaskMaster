package com.example.taksmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GestionTareasActivity extends AppCompatActivity {

    private RecyclerView rvTareas;
    private Spinner spCategoria;
    private Button btnNuevaTarea;
    private ProgressBar pbGlobal;
    private TextView tvTareasPendientes;
    private TareaAdapter adapter;
    
    // Lista estática para persistencia simple durante la ejecución
    public static List<Tarea> todasLasTareas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla2_gestion_tareas);

        rvTareas = findViewById(R.id.rvTareas);
        spCategoria = findViewById(R.id.spCategoria);
        btnNuevaTarea = findViewById(R.id.btnNuevaTarea);
        pbGlobal = findViewById(R.id.pbGlobal);
        tvTareasPendientes = findViewById(R.id.tvTareasPendientes);

        // Datos iniciales si la lista está vacía
        if (todasLasTareas.isEmpty()) {
            todasLasTareas.add(new Tarea("Informe Mensual", "Trabajo", "Alta", false, 4.0f, 50));
            todasLasTareas.add(new Tarea("Comprar pan", "Personal", "Baja", true, 2.0f, 100));
            todasLasTareas.add(new Tarea("Estudiar Android", "Estudio", "Media", false, 5.0f, 20));
        }

        adapter = new TareaAdapter(new ArrayList<>(todasLasTareas), new TareaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tarea tarea) {
                Intent intent = new Intent(GestionTareasActivity.this, VerTareaActivity.class);
                // Pasamos el índice para poder actualizarla
                intent.putExtra("posicion", todasLasTareas.indexOf(tarea));
                startActivity(intent);
            }
        });

        rvTareas.setLayoutManager(new LinearLayoutManager(this));
        rvTareas.setAdapter(adapter);

        // Spinner setup
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_filtro, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(spinnerAdapter);

        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] categoriasValores = getResources().getStringArray(R.array.categorias_filtro);
                filtrarTareas(categoriasValores[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnNuevaTarea.setOnClickListener(v -> {
            Intent intent = new Intent(GestionTareasActivity.this, CrearTareaActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar la lista al volver
        filtrarTareas(spCategoria.getSelectedItem().toString());
        actualizarProgresoGlobal();
    }

    private void filtrarTareas(String categoria) {
        List<Tarea> filtradas = new ArrayList<>();
        if (categoria.equals(getString(R.string.cat_todas))) {
            filtradas.addAll(todasLasTareas);
        } else {
            for (Tarea t : todasLasTareas) {
                if (t.getCategoria().equals(categoria)) {
                    filtradas.add(t);
                }
            }
        }
        adapter.updateList(filtradas);
    }

    private void actualizarProgresoGlobal() {
        int total = todasLasTareas.size();
        int completadas = 0;
        for (Tarea t : todasLasTareas) {
            if (t.isCompletada()) completadas++;
        }
        
        int progreso = total > 0 ? (completadas * 100 / total) : 0;
        pbGlobal.setProgress(progreso);
        tvTareasPendientes.setText(getString(R.string.lista_pendientes, (total - completadas)));
    }
}

package com.example.taksmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VerTareaActivity extends AppCompatActivity {

    private TextView tvNombre, tvCategoria, tvPrioridad, tvEstado;
    private ProgressBar pbAvance;
    private RatingBar rbImportancia;
    private CheckBox cbCompletada;
    private LinearLayout llSubtareasChecklist;
    private Button btnEditar, btnEliminar;
    private int posicion = -1;
    private Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla4_ver_tarea);

        tvNombre = findViewById(R.id.tvDetalleNombre);
        tvCategoria = findViewById(R.id.tvDetalleCategoria);
        tvPrioridad = findViewById(R.id.tvDetallePrioridad);
        tvEstado = findViewById(R.id.tvDetalleEstado);
        pbAvance = findViewById(R.id.pbAvance);
        rbImportancia = findViewById(R.id.rbDetalleImportancia);
        cbCompletada = findViewById(R.id.cbDetalleCompletada);
        llSubtareasChecklist = findViewById(R.id.llSubtareasChecklist);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        posicion = getIntent().getIntExtra("posicion", -1);

        cargarDatos();

        cbCompletada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (tarea != null && (tarea.getSubTareas() == null || tarea.getSubTareas().isEmpty())) {
                tarea.setCompletada(isChecked);
                tarea.setProgreso(isChecked ? 100 : 0);
                tvEstado.setText(isChecked ? "Completada" : "Pendiente");
                pbAvance.setProgress(tarea.getProgreso());
            } else if (isChecked) {
                // Si tiene subtareas, forzar completado solo si el usuario lo marca manualmente?
                // O mejor, el check principal solo funciona si no hay subtareas.
                Toast.makeText(this, "Completa las subtareas para avanzar", Toast.LENGTH_SHORT).show();
                cbCompletada.setChecked(tarea.isCompletada());
            }
        });

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(VerTareaActivity.this, CrearTareaActivity.class);
            intent.putExtra("posicion", posicion);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            if (posicion != -1) {
                GestionTareasActivity.todasLasTareas.remove(posicion);
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        if (posicion != -1 && posicion < GestionTareasActivity.todasLasTareas.size()) {
            tarea = GestionTareasActivity.todasLasTareas.get(posicion);
            tvNombre.setText(tarea.getNombre());
            tvCategoria.setText(tarea.getCategoria());
            tvPrioridad.setText(tarea.getPrioridad());
            
            int progresoActual = tarea.getProgreso();
            pbAvance.setProgress(progresoActual);
            tvEstado.setText(progresoActual == 100 ? "Completada" : "Pendiente");
            rbImportancia.setRating(tarea.getImportancia());
            
            cbCompletada.setOnCheckedChangeListener(null);
            cbCompletada.setChecked(progresoActual == 100);
            
            // Si hay subtareas, deshabilitar el check principal (el progreso depende de las subtareas)
            cbCompletada.setEnabled(tarea.getSubTareas().isEmpty());

            generarChecklist();
        }
    }

    private void generarChecklist() {
        llSubtareasChecklist.removeAllViews();
        if (tarea.getSubTareas() == null || tarea.getSubTareas().isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("Sin subtareas internas");
            llSubtareasChecklist.addView(tvVacio);
            return;
        }

        for (SubTarea st : tarea.getSubTareas()) {
            CheckBox cb = new CheckBox(this);
            cb.setText(st.getNombre());
            cb.setChecked(st.isCompletada());
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                st.setCompletada(isChecked);
                int nuevoProgreso = tarea.getProgreso();
                pbAvance.setProgress(nuevoProgreso);
                tvEstado.setText(nuevoProgreso == 100 ? "Completada" : "Pendiente");
                cbCompletada.setChecked(nuevoProgreso == 100);
                tarea.setCompletada(nuevoProgreso == 100);
            });
            llSubtareasChecklist.addView(cb);
        }
    }
}

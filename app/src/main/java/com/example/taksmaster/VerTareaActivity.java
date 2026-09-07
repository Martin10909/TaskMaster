package com.example.taksmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class VerTareaActivity extends AppCompatActivity {

    private TextView tvNombre, tvCategoria, tvPrioridad, tvEstado;
    private ProgressBar pbAvance;
    private RatingBar rbImportancia;
    private SwitchCompat swEstado;
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
        swEstado = findViewById(R.id.swEstado);
        llSubtareasChecklist = findViewById(R.id.llSubtareasChecklist);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        posicion = getIntent().getIntExtra("posicion", -1);

        cargarDatos();

        swEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (tarea != null) {
                if (tarea.getSubTareas() == null || tarea.getSubTareas().isEmpty()) {
                    tarea.setCompletada(isChecked);
                    tarea.setProgreso(isChecked ? 100 : 0);
                    actualizarUIEstado(isChecked);
                } else {
                    // Si tiene subtareas, no permitir cambiarlo manualmente si no están todas listas
                    int progreso = tarea.getProgreso();
                    if (progreso < 100 && isChecked) {
                        Toast.makeText(this, "Usa el checklist para completar la tarea", Toast.LENGTH_SHORT).show();
                        swEstado.setChecked(false);
                    } else if (progreso == 100 && !isChecked) {
                        // Permitir desmarcarla
                        tarea.setCompletada(false);
                        tarea.setProgreso(0);
                        actualizarUIEstado(false);
                    }
                }
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

    private void actualizarUIEstado(boolean completada) {
        tvEstado.setText(completada ? "Completada" : "Pendiente");
        pbAvance.setProgress(tarea.getProgreso());
        swEstado.setText(completada ? "Tarea Lista" : "Finalizar Tarea");
        // Evitar disparar el listener al cambiar el estado programáticamente
        swEstado.setOnCheckedChangeListener(null);
        swEstado.setChecked(completada);
        swEstado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (tarea.getSubTareas() == null || tarea.getSubTareas().isEmpty()) {
                tarea.setCompletada(isChecked);
                tarea.setProgreso(isChecked ? 100 : 0);
                actualizarUIEstado(isChecked);
            } else {
                if (isChecked && tarea.getProgreso() < 100) {
                    Toast.makeText(this, "Usa el checklist para completar la tarea", Toast.LENGTH_SHORT).show();
                    swEstado.setChecked(false);
                } else if (!isChecked) {
                    tarea.setCompletada(false);
                    tarea.setProgreso(0);
                    actualizarUIEstado(false);
                }
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
            actualizarUIEstado(progresoActual == 100);
            rbImportancia.setRating(tarea.getImportancia());
            
            // Si hay subtareas, el switch se bloquea para evitar cambios manuales que no coincidan con el progreso
            swEstado.setEnabled(tarea.getSubTareas().isEmpty() || progresoActual == 100);

            generarChecklist();
        }
    }

    private void generarChecklist() {
        llSubtareasChecklist.removeAllViews();
        if (tarea.getSubTareas() == null || tarea.getSubTareas().isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("Sin subtareas internas");
            tvVacio.setTextColor(getResources().getColor(R.color.palette_accent));
            llSubtareasChecklist.addView(tvVacio);
            return;
        }

        for (SubTarea st : tarea.getSubTareas()) {
            CheckBox cb = new CheckBox(this);
            cb.setText(st.getNombre());
            cb.setTextColor(getResources().getColor(R.color.palette_cream_text));
            cb.setButtonTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.palette_accent)));
            cb.setChecked(st.isCompletada());
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                st.setCompletada(isChecked);
                int nuevoProgreso = tarea.getProgreso();
                tarea.setCompletada(nuevoProgreso == 100);
                actualizarUIEstado(nuevoProgreso == 100);
            });
            llSubtareasChecklist.addView(cb);
        }
    }
}

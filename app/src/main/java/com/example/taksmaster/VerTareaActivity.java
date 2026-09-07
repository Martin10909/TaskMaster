package com.example.taksmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        posicion = getIntent().getIntExtra("posicion", -1);

        cargarDatos();

        cbCompletada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (tarea != null) {
                tarea.setCompletada(isChecked);
                tarea.setProgreso(isChecked ? 100 : 0);
                tvEstado.setText(isChecked ? "Completada" : "Pendiente");
                pbAvance.setProgress(tarea.getProgreso());
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
            tvEstado.setText(tarea.isCompletada() ? "Completada" : "Pendiente");
            pbAvance.setProgress(tarea.getProgreso());
            rbImportancia.setRating(tarea.getImportancia());
            
            // Evitar que el listener se dispare al setear el estado inicial
            cbCompletada.setOnCheckedChangeListener(null);
            cbCompletada.setChecked(tarea.isCompletada());
            cbCompletada.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tarea.setCompletada(isChecked);
                tarea.setProgreso(isChecked ? 100 : 0);
                tvEstado.setText(isChecked ? "Completada" : "Pendiente");
                pbAvance.setProgress(tarea.getProgreso());
            });
        }
    }
}

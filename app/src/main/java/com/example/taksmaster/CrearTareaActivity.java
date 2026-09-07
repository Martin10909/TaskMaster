package com.example.taksmaster;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CrearTareaActivity extends AppCompatActivity {

    private ImageView ivIcono;
    private EditText etNombre;
    private Spinner spCategoriaForm;
    private RadioGroup rgPrioridad;
    private CheckBox cbCompletada;
    private RatingBar rbImportancia;
    private Button btnGuardar, btnCancelar;
    private int posicionEdicion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla3_crear_tarea);

        ivIcono = findViewById(R.id.ivIcono);
        etNombre = findViewById(R.id.etNombre);
        spCategoriaForm = findViewById(R.id.spCategoriaForm);
        rgPrioridad = findViewById(R.id.rgPrioridad);
        cbCompletada = findViewById(R.id.cbCompletada);
        rbImportancia = findViewById(R.id.rbImportancia);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        String[] categorias = {"Trabajo", "Personal", "Estudio"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoriaForm.setAdapter(adapter);

        // Pre-cargar datos si es edición
        posicionEdicion = getIntent().getIntExtra("posicion", -1);
        if (posicionEdicion != -1) {
            Tarea tareaEdicion = GestionTareasActivity.todasLasTareas.get(posicionEdicion);
            etNombre.setText(tareaEdicion.getNombre());
            for (int i = 0; i < categorias.length; i++) {
                if (categorias[i].equals(tareaEdicion.getCategoria())) {
                    spCategoriaForm.setSelection(i);
                    break;
                }
            }
            cbCompletada.setChecked(tareaEdicion.isCompletada());
            rbImportancia.setRating(tareaEdicion.getImportancia());
            if ("Alta".equals(tareaEdicion.getPrioridad())) rgPrioridad.check(R.id.rbAlta);
            else if ("Media".equals(tareaEdicion.getPrioridad())) rgPrioridad.check(R.id.rbMedia);
            else if ("Baja".equals(tareaEdicion.getPrioridad())) rgPrioridad.check(R.id.rbBaja);
        }

        ivIcono.setOnClickListener(v -> {
            Toast.makeText(this, "Seleccionar ícono (Funcionalidad futura)", Toast.LENGTH_SHORT).show();
        });

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String categoria = spCategoriaForm.getSelectedItem().toString();
            String prioridad = "Media";
            int idCheck = rgPrioridad.getCheckedRadioButtonId();
            if (idCheck == R.id.rbAlta) prioridad = "Alta";
            else if (idCheck == R.id.rbBaja) prioridad = "Baja";

            boolean completada = cbCompletada.isChecked();
            float importancia = rbImportancia.getRating();

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show();
                return;
            }

            if (posicionEdicion != -1) {
                // Actualizar
                Tarea t = GestionTareasActivity.todasLasTareas.get(posicionEdicion);
                t.setNombre(nombre);
                t.setCategoria(categoria);
                t.setPrioridad(prioridad);
                t.setCompletada(completada);
                t.setImportancia(importancia);
                if (completada) t.setProgreso(100);
            } else {
                // Crear nueva
                int progreso = completada ? 100 : 0;
                Tarea nueva = new Tarea(nombre, categoria, prioridad, completada, importancia, progreso);
                GestionTareasActivity.todasLasTareas.add(nueva);
            }

            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancelar.setOnClickListener(v -> finish());
    }
}

package id.ac.polbeng.tamarablezinky.sqliteexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.ac.polbeng.tamarablezinky.sqliteexample.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_STUDENT = "extra_student"
    }

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var studentDBHelper: StudentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val studentData = intent.getParcelableExtra<StudentModel>(EXTRA_STUDENT) as StudentModel
        binding.etNIM.isEnabled = false
        binding.etNIM.setText(studentData.nim)
        binding.etNama.setText(studentData.name)
        binding.etUmur.setText(studentData.age)

        studentDBHelper = StudentDBHelper(this)

        binding.btnUpdate.setOnClickListener {
            val nim = binding.etNIM.text.toString()
            val name = binding.etNama.text.toString()
            val age = binding.etUmur.text.toString()

            if (nim.isEmpty() || name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan data NIM, nama, dan umur!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUpdateStudent = StudentModel(nim = nim, name = name, age = age)
            val updateCount = studentDBHelper.updateStudent(newUpdateStudent)

            if (updateCount > 0) {
                Toast.makeText(this, "Data mahasiswa terupdate: $updateCount", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Tidak ada data mahasiswa yang diupdate, silakan coba lagi!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHapus.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Hapus Data")
                .setMessage("Apakah Anda yakin?")
                .setPositiveButton("Ya") { _, _ ->
                    val nim = binding.etNIM.text.toString()
                    val deleteCount = studentDBHelper.deleteStudent(nim)
                    if (deleteCount > 0) {
                        Toast.makeText(this, "Data mahasiswa terhapus: $deleteCount", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Tidak ada data mahasiswa yang dihapus, silakan coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Tidak", null)

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onDestroy() {
        studentDBHelper.close()
        super.onDestroy()
    }
}

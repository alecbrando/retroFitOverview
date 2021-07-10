package com.alecbrando.retrofitcrashcourse

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alecbrando.retrofitcrashcourse.Adapters.TodoAdapter
import com.alecbrando.retrofitcrashcourse.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            } catch(e: IOException){
                Log.d("MainActivity", e.toString())
                return@launchWhenCreated
            } catch(e: HttpException){
                Log.d("MainActivity", e.toString())
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                todoAdapter.todos = response.body()!!
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun setUpRecyclerView() = binding.rvTodos.apply  {
        todoAdapter = TodoAdapter()
        adapter = todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}
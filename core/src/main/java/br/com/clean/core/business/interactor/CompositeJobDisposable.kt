package br.com.clean.core.business.interactor

import kotlinx.coroutines.Job
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CompositeJobDisposable {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val executor = Executors.newSingleThreadExecutor()
    private val list: MutableList<Job> = mutableListOf()

    init {
        scheduler.scheduleAtFixedRate(::purge, 2, 2, TimeUnit.MINUTES)
    }

    fun add(job: Job?) {
        if (job != null) executor.execute { list.add(job) }
    }

    fun remove(job: Job) {
        executor.execute { list.remove(job) }
    }

    fun cancel() {
        executor.execute {
            list.forEach {
                try {
                    if (it.isActive) it.cancel()
                } finally {
                }
            }
            list.clear()
        }
    }

    private fun purge() {
        executor.execute {
            if (list.size > 0) {
                val filtered = list.filter { it.isCancelled || it.isCompleted }
                list.removeAll(filtered as Collection<Job>)
            }
        }
    }
}
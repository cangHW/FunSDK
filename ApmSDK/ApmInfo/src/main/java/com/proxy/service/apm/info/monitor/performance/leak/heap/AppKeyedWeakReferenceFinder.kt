package com.proxy.service.apm.info.monitor.performance.leak.heap

import com.proxy.service.apm.info.monitor.performance.leak.watcher.reference.KeyedWeakReference
import shark.HeapGraph
import shark.HeapObject.HeapInstance
import shark.LeakingObjectFinder

/**
 * 在 hprof 中定位本模块 KeyedWeakReference 仍持有 referent 的对象。
 */
object AppKeyedWeakReferenceFinder : LeakingObjectFinder {

    private  val KEYED_WEAK_REFERENCE_CLASS = KeyedWeakReference::class.java.name

    override fun findLeakingObjectIds(graph: HeapGraph): Set<Long> {
        val result = LinkedHashSet<Long>()
        val keyedClass = graph.findClassByName(KEYED_WEAK_REFERENCE_CLASS) ?: return result
        keyedClass.instances.forEach { instance ->
            val referentId = instance.readReferentObjectId() ?: return@forEach
            result.add(referentId)
        }
        return result
    }

    private fun HeapInstance.readReferentObjectId(): Long? {
        val referentField = this["java.lang.ref.Reference", "referent"] ?: return null
        if (referentField.value.isNullReference) {
            return null
        }
        val objectId = referentField.value.asObjectId
        if (objectId == 0L) {
            return null
        }
        return objectId
    }
}

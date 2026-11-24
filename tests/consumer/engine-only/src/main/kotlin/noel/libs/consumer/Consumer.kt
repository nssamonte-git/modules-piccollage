package noel.libs.consumer
import noel.libs.creative.model.*
import noel.libs.creative.history.domain.HistorySession
fun createSession():EditSession=HistorySession(CreativeDocument("consumer",pages=listOf(Page("p",100,100))))

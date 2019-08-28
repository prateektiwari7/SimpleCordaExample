package com.template.webserver

import net.corda.core.identity.CordaX500Name
import net.corda.core.messaging.startTrackedFlow
import net.corda.core.utilities.getOrThrow
import org.slf4j.LoggerFactory
import com.template.flows.NameFlow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

val SERVICE_NAMES = listOf("Notary", "Network Map Service")
/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/name/api") // The paths for HTTP requests are relative to this base path.
class Controller(rpc: NodeRPCConnection) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    private val Legalnamenode = rpc.proxy.nodeInfo().legalIdentities.first().name
    private val proxy = rpc.proxy

    /*
    Returns the name of your node
    * */
    @GetMapping(value=["peer"],produces = [ "application/json" ])
    fun nodename() = mapOf("peer" to Legalnamenode)

    @GetMapping(value=["peers"], produces = ["application/json"])
    fun getpeers(): Map<String , List<CordaX500Name>>{
        val nodeInfo = proxy.networkMapSnapshot()
        return mapOf("peers" to nodeInfo.map { it.legalIdentities.first().name }.filter { it.organisation !in (SERVICE_NAMES + Legalnamenode.organisation) }
        )
    }

    @PostMapping(value = ["post-name"], produces = ["text/plain"] , headers = ["Content-Type=application/x-www-form-urlencoded"])
    fun createname(request:HttpServletRequest):ResponseEntity<String>{
        val name = request.getParameter("name")
        val lastname = request.getParameter("lastname")
        val party = request.getParameter("partyName")

        if(party == null){
            return ResponseEntity.badRequest().body("Query parameter 'partyName' must not be null.\n")
        }

        val partyX500Name = CordaX500Name.parse(party)
        val otherParty = proxy.wellKnownPartyFromX500Name(partyX500Name) ?: return ResponseEntity.badRequest().body("Party named $party cannot be found.\n")

        return try{
            val signedTx = proxy.startTrackedFlow(::NameFlow.also {  },name,lastname,otherParty).returnValue.getOrThrow()

            ResponseEntity.status(HttpStatus.CREATED).body("Transaction id ${signedTx.id} committed to ledger.\n")

        } catch (ex:Throwable) {

            logger.error(ex.message, ex)
            ResponseEntity.badRequest().body(ex.message!!)
        }




    }


    @GetMapping(value = "/templateendpoint", produces = arrayOf("text/plain"))
    private fun templateendpoint(): String {
        return "Define an endpoint here."
    }
}
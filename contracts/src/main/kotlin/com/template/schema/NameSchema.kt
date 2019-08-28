package com.template.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Persistence
import javax.persistence.Table


object NameSchema

object NameSchema1 : MappedSchema(
        schemaFamily = NameSchema.javaClass,
        version = 1,
        mappedTypes =  listOf(NAMEPERT::class.java)){
            @Entity
            @Table(name = "Prateek")
                    class NAMEPERT (
                    @Column(name = "name")
                    var name : String,
                    @Column(name = "lastname" )
                    var lastname : String,
                    @Column(name = "party")
                    var party : String,
                    @Column(name = "linear_id")
                    var linearID : UUID
            ) : PersistentState (){
                constructor() : this ("","","",UUID.randomUUID());
            }
        }

package com.org.hdfs.file.listener

import java.net.URI
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hdfs.client.HdfsAdmin
import org.apache.hadoop.hdfs.inotify.Event.{AppendEvent, CreateEvent, RenameEvent}

object HdfsFileListener extends App  {

  val admin = new HdfsAdmin(URI.create(args.apply(0)), new Configuration() )
  val eventStream = admin.getInotifyEventStream()
  try {
    while( true ) {
      val events =  eventStream.poll(2l, java.util.concurrent.TimeUnit.SECONDS)
      events.getEvents.toList.foreach { event ⇒
        println(s"event type = ${event.getEventType}")
        event match {
          case create: CreateEvent ⇒
            println("CREATE: " + create.getPath)

          case rename: RenameEvent ⇒
            println("RENAME: " + rename.getSrcPath + " => " + rename.getDstPath)

          case append: AppendEvent ⇒
            println("APPEND: " + append.getPath)

          case other ⇒
            println("other: " + other)
        }
      }
    }

  }
  catch {
    case exc: Exception =>
      println("Unable to start file events", exc)
      sys.exit(1)
  }


}


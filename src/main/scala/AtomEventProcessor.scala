import com.gu.contentatom.thrift.{Atom, AtomType}
import com.gu.crier.model.event.v1.EventType


object AtomEventProcessor {
  def decodeAtomType(t: AtomType):String = t match {
    case AtomType.Media=>"Media"
    case AtomType.Cta=>"CTA"
    case AtomType.Explainer=>"Explainer"
    case AtomType.Guide=>"Guide"
    case AtomType.Interactive=>"Interactive"
    case AtomType.Profile=>"Profile"
    case AtomType.Qanda=>"Q&A"
    case AtomType.Quiz=>"Quiz"
    case AtomType.Recipe=>"Recipe"
    case AtomType.Review=>"Review"
    case AtomType.Storyquestions=>"Story Questions"
    case AtomType.Timeline=>"Timeline"
    case _=>"unknown"
  }

  def process(atom: Atom, eventType: EventType):Boolean = {
    println(s"Got atom title ${atom.title} event $eventType")
    atom.atomType match {
      case AtomType.Media=>
        println("Got a media atom!")
        println(s"Title: ${atom.title}, event: $eventType")
        true
      case _=>
        println(s"Got some other kinda atom: ${decodeAtomType(atom.atomType)}, bored, going home.")
        false
    }
  }
}

package ehrcommitter

import java.util.UUID
import java.text.SimpleDateFormat

class EhrformTagLib {
    //static defaultEncodeAs = [taglib:'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    
    // Do not generate inputs for these names
    def filter = ["PATIENT_UID"]
    
    def display = { attrs, body ->
    
       //println "attrs "+ attrs
    
       // [[name::type]]
       // name::type
       // name, type
       def (name, type, defaultValue) = attrs.tag[2..-3].split(':::')
       
       if (filter.contains(name)) return
       
       def method = "display"+type
       out << this."$method"(name, defaultValue)
    }
    
    def displaySTRING(String name, String defaultValue) {
    
       if (defaultValue == "RANDOM")
       {
          defaultValue = generator( (('A'..'Z')+('a'..'z')+' ').join(), 30 )
       }
       else if (defaultValue == "EMPTY")
       {
          defaultValue = ""
       }
    
       """<label>${name}
       <textarea name=\"${name}\">${defaultValue}</textarea>
       </label><br />
       """
    }
    
    def displayDATE(String name, String defaultValue) {
    
       if (defaultValue == "NOW")
       {
          def now = new Date()
          //def date_format = "yyyyMMdd" // openEHR format
          def date_format = "yyyy-MM-dd" // HTML5 format 
          def formatter = new SimpleDateFormat( date_format )
          defaultValue = formatter.format( now )
       }
       else
          defaultValue = ''
          
       """<label>${name}
       <input type=\"date\" name=\"${name}\" value=\"${defaultValue}\" />
       </label><br />
       """
    }
    
    def displayDATETIME(String name, String defaultValue) {
    
       def defaultValueDate, defaultValueTime
    
       if (defaultValue == "NOW")
       {
          def now = new Date()
          //def datetime_format = "yyyyMMdd'T'HHmmss,SSSZ" // openEHR format
          def datetime_format = "yyyy-MM-dd HH:mm:ss" // HTML5 format
          def formatter = new SimpleDateFormat( datetime_format )
          def datetime = formatter.format( now )
          (defaultValueDate, defaultValueTime) = datetime.split(' ')
       }
       else
       {
          defaultValueDate = ''
          defaultValueTime = ''
       }
    
       """<label>${name}
       <input type=\"date\" name=\"${name}\" value=\"${defaultValueDate}\" />
       <input type=\"time\" name=\"${name}\" value=\"${defaultValueTime}\" />
       </label><br />
       """
    }
    
    def displayUUID(String name, String defaultValue) {
    
       if (defaultValue == "ANY")
          defaultValue = UUID.randomUUID().toString()
       
       """<label>${name}
       <input type=\"text\" name=\"${name}\" value=\"${defaultValue}\" />
       </label><br />
       """
    }
    
    // Generates an OBJECT_VERSION_ID object_id ‘::’ creating_system_id ‘::’ version_tree_id
    //[[VERSION:::UUID:::ANY]]::[[APP:::STRING:::EMR]]::1
    def displayVERSION_ID(String name, String defaultValue) {
    
       if (defaultValue == "ANY")
          defaultValue = UUID.randomUUID().toString() +"::EMR::1"
       
       """<label>${name}
       <input type=\"text\" name=\"${name}\" value=\"${defaultValue}\" />
       </label><br />
       """
    }
    
    // generator( (('A'..'Z')+('0'..'9')).join(), 9 )
    def generator = { String alphabet, int n ->
       new Random().with {
         (1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
       }
    }
}

package nextflow.validation

import groovy.util.logging.Slf4j
import groovy.transform.PackageScope


/**
 * This class allows model an specific configuration, extracting values from a map and converting
 *
 * We annotate this class as @PackageScope to restrict the access of their methods only to class in the
 * same package
 *
 * @author : nvnieuwk <nicolas.vannieuwkerke@ugent.be>
 *
 */

@Slf4j
@PackageScope
class ValidationConfig {

    final public Boolean lenientMode
    final public Boolean monochromeLogs
    final public Boolean failUnrecognisedParams
    final public Boolean failUnrecognisedHeaders
    final public String  parametersSchema
    final public Boolean showHiddenParams
    final public HelpConfig help
    final public SummaryConfig summary

    final public List<String> ignoreParams

    ValidationConfig(Map map, Map params){
        def config = map ?: Collections.emptyMap()
        lenientMode             = config.lenientMode                ?: false
        monochromeLogs          = config.monochromeLogs             ?: false
        failUnrecognisedParams  = config.failUnrecognisedParams     ?: false
        failUnrecognisedHeaders = config.failUnrecognisedHeaders    ?: false
        showHiddenParams        = config.showHiddenParams           ?: false
        if(config.containsKey("showHiddenParams")) {
            log.warn("configuration option `validation.showHiddenParams` is deprecated, please use `validation.help.showHidden` or the `--showHidden` parameter instead")
        }
        parametersSchema        = config.parametersSchema       ?: "nextflow_schema.json"
        help                    = new HelpConfig(config.help as Map ?: [:], params, monochromeLogs, showHiddenParams)
        summary                 = new SummaryConfig(config.summary as Map ?: [:], monochromeLogs)

        if(config.ignoreParams && !(config.ignoreParams instanceof List<String>)) {
            throw new SchemaValidationException("Config value 'validation.ignoreParams' should be a list of String values")
        }
        ignoreParams = config.ignoreParams ?: []
        if(config.defaultIgnoreParams && !(config.defaultIgnoreParams instanceof List<String>)) {
            throw new SchemaValidationException("Config value 'validation.defaultIgnoreParams' should be a list of String values")
        }
        ignoreParams += config.defaultIgnoreParams ?: []
        ignoreParams += 'nf_test_output' //ignore `nf_test_output` directory when using nf-test

    }
}
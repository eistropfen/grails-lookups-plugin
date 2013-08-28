package org.grails.plugins.lookups

import org.grails.plugins.lookups.*

class LookupTagLib {

    def lookupService

    // maintain backward compatibility
    def lookupSelect = {attrs, body ->
        out << g.lookupSelectByCode(attrs)
    }

    def lookupSelectByCode = {attrs, body ->
        def realm = attrs.realm
        if (realm) {
            attrs.remove("realm")
            attrs.remove("keys")
            attrs.remove("valueMessagePrefix")
            attrs.from = Lookup.codeList(realm)
            attrs.optionKey = "code"
            attrs.optionValue = "value"
            out << g.select(attrs)
        }
    }

    def lookupSelectById = {attrs, body ->
        def realm = attrs.realm
        if (realm) {
            attrs.remove("realm")
            attrs.remove("keys")
            attrs.remove("valueMessagePrefix")
            attrs.from = Lookup.idList(realm)
            attrs.optionKey = "code"
            attrs.optionValue = "value"
            out << g.select(attrs)
        }
    }

    def lookupValue = {attrs, body ->
        out << g.lookupValueByCode(attrs)
    }

    def lookupValueByCode = {attrs, body ->
        def val = Lookup.valueForCode(attrs.realm, attrs.value, attrs.default)

        if (val != null) {

            val = val.toString()
            if (attrs.encodeAs) {
                switch (attrs.encodeAs.toUpperCase()) {

                    case "HTML":
                        val = val.encodeAsHTML()
                        break

                    case "XML":
                        val = val.encodeAsXML()
                        break
                }
            }

            out << val
        }
    }

    def lookupValueById = {attrs, body ->
        def val = Lookup.valueForId(attrs.realm, attrs.value, attrs.default)

        if (val != null) {

            val = val.toString()
            if (attrs.encodeAs) {
                switch (attrs.encodeAs.toUpperCase()) {

                    case "HTML":
                        val = val.encodeAsHTML()
                        break

                    case "XML":
                        val = val.encodeAsXML()
                        break
                }
            }

            out << val
        }
    }

    def lookupHelpBalloons = {attrs, body ->
        if (lookupService.hasPlugin("helpBalloons")) {
            out << g.helpBalloons(attrs)
        }
    }

    def lookupHelpBalloon = {attrs, body ->
        if (lookupService.hasPlugin("helpBalloons")) {
            out << g.helpBalloon(attrs)
        }
    }

    def lookupCriteria = {attrs, body ->
        if (lookupService.hasPlugin("criteria")) {
            out << """<div class="criteria">\n"""
            out << g.criteria(attrs)
            out << """</div>\n"""
        }
    }

    def lookupPaginate = {attrs, body ->
        def count = (lookupService.hasPlugin("criteria") || lookupService.hasPlugin("drilldowns")) ? Lookup.selectCount(session, params) : Lookup.count()
        out << g.paginate(total: count)
    }

    def lookupValuePaginate = {attrs, body ->
        def count = (lookupService.hasPlugin("criteria") || lookupService.hasPlugin("drilldowns")) ? LookupValue.selectCount(session, params) : LookupValue.count()
        out << g.paginate(total: count)
    }

    def lookupDrilldown = {attrs, body ->
        if (lookupService.hasPlugin("drilldowns")) {
            out << g.drilldown(attrs)
        }
    }

    def lookupDrilldownReturn = {attrs, body ->
        if (lookupService.hasPlugin("drilldowns")) {
            out << g.drilldownReturn(attrs)
        }
    }

    def lookupHeading = {attrs, body ->
        if (lookupService.hasPlugin("drilldowns")) {
            out << """<th>${g.message(code: "lookup.values", default: "Values")}</th>"""
        }
    }

    def lookupData = {attrs, body ->
        if (lookupService.hasPlugin("drilldowns")) {
            out << """<td>${g.drilldown(controller: "lookupValue", action: "list", value: attrs.value)}</td>"""
        }
    }

    def lookupMenuButton = {attrs, body ->
        if (lookupService.hasPlugin("menus")) {
            out << '<span class="menuButton">'
            out << g.link(class: "menu", controller: "menu", action: "display") {
                g.message(code: "menu.display", default: "Menu")
            }
            out << '</span>'
        }
    }

    def lookupResource = {attrs, body ->
        def uri = grailsAttributes.getApplicationUri(request)
        if (attrs.dir) uri = uri + '/' + attrs.dir
        if (attrs.file) uri = uri + '/' + attrs.file
        out << uri
    }
}

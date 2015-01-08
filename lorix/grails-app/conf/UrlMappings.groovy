class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'workspace', action:'index')
        "/org/${shortcode}/$action" (controller:'org')

        "500"(view:'/error')
	}
}

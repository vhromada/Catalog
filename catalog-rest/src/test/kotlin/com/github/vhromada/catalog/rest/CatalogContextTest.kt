package com.github.vhromada.catalog.rest

import com.github.vhromada.common.test.AppContextTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
class CatalogContextTest : AppContextTest()

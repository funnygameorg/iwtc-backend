rootProject.name = "itwc"

include(":service:worldcupgame")
include(":service:member")

include(":core:db")
include(":core:jwt")
include(":core:caffeine")
include(":core:redis")
include(":core:feign")
include(":core:error")
include(":core:encoder")

include(":domain:model")
include("core:encoder")
include("core:rememberme")

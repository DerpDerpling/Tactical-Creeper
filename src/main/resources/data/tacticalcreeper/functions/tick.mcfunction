execute at @a run effect give @e[distance=0.1..5.7, type=tacticalcreeper:tacticalcreeper] minecraft:levitation 1 7 true
execute at @e[type=player] run data merge entity @e[limit=1, distance=0.1..5.7, type=tacticalcreeper:tacticalcreeper] {ignited:1b, Fuse:60s}
# GmLrdLib

This is a library that I will fill with "useful" pieces of code for my mods. (Right now mod, but you never know.) Here is a handy list of whats currently in the mod:

- A convoluded patch for [MC-265322](https://bugs.mojang.com/browse/MC-265322)

## Installation instructions

To install it, add the following to your build.gradle file:

```groovy
dependencies {
    modImplementation 'io.github.gamelord2011.gmlrdlib:gmlrdlib-1.0.1'
}
```

## Usage

Readthedocs.

## Why does this exist

To be honest, because I saw an extremely specific gap that needed to be filled by some sort of tool, and there was nothing. I used the fabric toolchain and leveraged spongepowered's mixin project to allow this to work.

## Demo

### GmlrdLang

[AGSR's lang system redone with this](https://github.com/GameLord2011/Anti-Grian-Switch-Reborn/tree/UsingGmlrdLib)

## Aren't there more efficent ways to do these things???

### GmlrdLang

Undoubtedly, there are several better ways to do this. For example, I could take [Wurst Imperium's approach](https://github.com/Wurst-Imperium/Wurst7/blob/master/src/main/java/net/wurstclient/WurstTranslator.java) and just reimplement the vanilla translation system in it's entirety. Or I could take an approach similar to [NikOverflow's method](https://github.com/NikOverflow/ExploitPreventer/blob/master/src/main/java/com/nikoverflow/exploitpreventer/injection/mixin/vanilla/AbstractSignEditScreenMixin.java) and block signs and whatnot from reading them, but I wanted to take my own approach.

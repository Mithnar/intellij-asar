macro do_something()
    ?MacroMainLabel:
    ?.MacroSubLabel
    ?-
        ; All of these are fine!
        dl ?MacroMainLabel
        dl ?.MacroSubLabel
        dl ?-
        dl ?+
        dl ?MacroMainLabel_MacroSubLabel
    ?+
endmacro

%do_something()
dl ?MacroMainLabel
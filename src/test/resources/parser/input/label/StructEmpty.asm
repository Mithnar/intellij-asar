; Edge cases for struct parsing:
; 1. A struct with no fields (empty body)
; 2. A struct whose name looks keyword-like
; 3. Back-to-back structs with no blank line between them
; 4. A struct with a negative skip

struct Empty $7E0000
endstruct

struct align_data $7E0010
.Size: skip 4
endstruct

struct PacketHeader $7E0020
.Type: skip 1
.Length: skip 1
endstruct
struct PacketBody $7E0022
.Data: skip 8
endstruct

struct Backfill $7E0100
.Marker: skip -1
endstruct

lda align_data.Size
lda PacketHeader.Type
lda PacketHeader.Length
lda PacketBody.Data
lda Backfill.Marker
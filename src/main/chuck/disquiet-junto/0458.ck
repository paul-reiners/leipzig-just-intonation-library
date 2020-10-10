[[[ 25,  18], [25,  24], [25, 16], [75, 64], [225, 128]],
 [[ 10,   9], [ 5,   3], [ 5,  4], [15,  8], [ 45,  32]],
 [[ 16,   9], [ 4,   3], [ 1,  1], [ 3,  2], [  9,   8]],
 [[ 64,  45], [16,  15], [ 8,  5], [ 6,  5], [  9,   5]],
 [[256, 225], [128, 75], [32, 25], [48, 25], [ 36,  25]]] 
     @=> int fiveLimitLattice[][][];
     
float pitches[5][5];

440.0 => float middleA;

for (0 => int i; i < 5; i++) {
    for (0 => int j; j < 5; j++) {
        fiveLimitLattice[i][j] @=> int interval[];
         middleA * interval[0] / interval[1] => pitches[i][j];
    }
}


Moog moog0 => pan2 p0 => dac;
-1 => p0.pan;
Moog moog1 => pan2 p1 => dac;
0 => p1.pan;
Moog moog2 => pan2 p2 => dac;
1 => p2.pan;
0.8 => moog0.noteOn;
0.8 => moog1.noteOn;
0.8 => moog2.noteOn;

fun void playNote(float freq, int index) {
    freq / 2.0 => freq;
    if (index == 0) {
        freq => moog0.freq;
    } else if (index == 1) {
        freq => moog1.freq;
    } else {
        freq => moog2.freq;
    }
}

2::second => dur note;

float spiralPitches[25];
0 => int x;
0 => int y;
0 => int i;

pitches[x + 2][y + 2] => spiralPitches[i++];
for (0 => int inner; inner < 2; inner++) {    
    // Go up
    while (y <= inner) {
        y + 1 => y;
        pitches[x + 2][y + 2] => spiralPitches[i++];
    } 
    // Go right
    while (x <= inner) {     
        x + 1 => x;
        pitches[x + 2][y + 2] => spiralPitches[i++];
    }
    // Go down
    while (y >= -inner) {    
        y - 1 => y;
        pitches[x + 2][y + 2] => spiralPitches[i++];
    }
    // Go left
    while (x >= -inner) {
        x - 1 => x;
        pitches[x + 2][y + 2] => spiralPitches[i++];
    } 
    // Go up
    while (y <= inner) {    
        y + 1 => y;
        pitches[x + 2][y + 2] => spiralPitches[i++];
    }    
}

0 => int j;
for (0 => i; i < 25; i++) {
    note => now;
    playNote(spiralPitches[i], j);
    (j + 1) % 3 => j;
}
for (21 => i; i >= 0; i--) {
    note => now;
    playNote(spiralPitches[i], j);
    (j + 1) % 3 => j;
}
for (0 => i; i < 3; i++) {
    if (j == 0) {
        1 => moog0.noteOff;
    } else if (j == 1) {
        1 => moog1.noteOff;
    } else {
        1 => moog2.noteOff;
    }
    (j + 1) % 3 => j;
}

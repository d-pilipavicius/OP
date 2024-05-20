/*Imported block variables*/
int imgW, imgH;
int imgTileX = 32, imgTileY = 32;
int[] solidsID;
PImage tiles;
PImage[] blocks;
String[] solids;


/*Player variables*/
int playerX = 0, playerY = 0;
int playerW = 32, playerH = 32;
int startedJumping;
int startedFalling;
int diedOnFrame;
int fallingSpeed = 0;
int[][] playerSpawn;
PImage playerFrames;
PImage[] plrBlocks;
boolean goingRight = false, goingLeft = false;
boolean goingUp = false, goingDown = false;
boolean dead = false;
boolean win = false;

/*Menu variables*/
color menuColor = color(180);
Button[] buttons;

/*World variables*/
int nrOfLevels = 5;
int worldWidth = 100, worldHeight = 30;
int[][][] grid;
int blockX = 32, blockY = 32;
color skyColor = color(85, 180, 255);
String[] txtFileLines;
int whichWorld = -1;
int translX, translY;
boolean editor = false;
int[][] doorCoords;
boolean[] isDoor;

void setup()
{
  size(800, 800);
  grid = new int[nrOfLevels][worldHeight][worldWidth];
  buttons = new Button[nrOfLevels];
  playerSpawn = new int[nrOfLevels][2];
  doorCoords = new int[nrOfLevels][2];
  isDoor = new boolean[nrOfLevels];
  for(int i = 0; i < nrOfLevels; ++i)
  {
    buttons[i] = new Button(width/5, height/5+i*height*51/400, width*3/5, height*9/100, color(0,255,0), color(150,255,150), "Level "+(i+1));
    isDoor[i] = false;
  }
  save = new Button(width/4, height*10/11, width/2, height/11, color(255, 255, 0), color(255, 255, 150), "Save level");
  loadBlocks();
  loadPlayer();
  loadSolidBlocks();
  for(int i = 0; i < nrOfLevels; ++i)
  {
    loadTxtToWorld(i);
  }
}

void draw()
{
  if(whichWorld == -1)
  {
    editorX = 0;
    editorY = 0;
    displayMenu();
  }
  else
  {
    if(!editor)
    {
      if(!checkForWin())
      {
        win = false;
      blockChosen = -1;
      spawnpointRelocation = false;
      choosingBlock = false;
      gravity();
      displayWorld();
      controlPlayer();
      displayPlayer();
      deathScreen();
      }
      else
      {
        if(!win)
        {
          win = true;
          diedOnFrame = frameCount;
        }
        if(win && frameCount-diedOnFrame < 300)
        {
          fill(0);
          textSize(height/10);
          text("YOU WIN", width/2, height/2);
        }
        else if(win)
        {
          win = false;
          whichWorld = -1;
        }
      }
    }
    else
    {
      displayEditor();
    }
  }
}

void loadBlocks()
{
  tiles = loadImage("tiles.png");
  imgW = tiles.width/blockX;
  imgH = tiles.height/blockY;
  blocks = new PImage[imgW*imgH+1];
  for(int i = 0 ; i < imgH; ++i)
  {
    for(int j = 0; j < imgW; ++j)
    {
      blocks[i*imgW+j] = tiles.get(j*imgTileX, i*imgTileY, imgTileX, imgTileY);
    }
  }
  blocks[blocks.length-1] = loadImage("LevelClear.png");
}

void loadSolidBlocks()
{
  solids = loadStrings("solids.txt");
  solidsID = new int[solids.length];
  for(int i = 0; i < solidsID.length; ++i)
  {
    solidsID[i] = Integer.valueOf(solids[i]);
  }
}

void keyPressed()
{
  switch(key)
  {
    case 'e':
    case 'E':
      if(whichWorld != -1)
      {
        key = '\\';
        editor = !editor;
      }
      break;
    case 'x':
    case 'X':
      editor = false;
      whichWorld = -1;
      break;
  }
}

boolean choosingBlock = false;
boolean spawnpointRelocation = false;
boolean pressedButton = false;
int editorX, editorY;
int blockChosen = -1;

int editBlockX = 48;
int editBlockY = 48;
Button save;

void displayEditor()
{
  if(!choosingBlock)
  {
    displayWorld();
    displayGrid();
    image(plrBlocks[10], playerSpawn[whichWorld][0], playerSpawn[whichWorld][1]);
    changeBlocks();
  }
  else
  {
    displayBlockChoice();
    pickBlock();
    save.displayButton();
    if(save.isClicked)
    {
      writeOutWorld(grid[whichWorld], whichWorld);
      editor = false;
    }
  }
  displayChosen();
  displayBlockButton();
}

void displayBlockButton()
{
  noStroke();
  if(!mousePressed)
    pressedButton = false;
  if(mouseX+height/20-width-mouseY < 0)
  {
    fill(255, 0, 0);
  }
  else if(mousePressed && !pressedButton)
  {
    fill(255, 150, 150);
    choosingBlock = !choosingBlock;
    pressedButton = true;
  }
  else
  {
    fill(255, 75, 75);
  }
  if(!choosingBlock) triangle(-editorX+width-height/20, -editorY, -editorX+width, -editorY+height/20, -editorX+width, -editorY);
  else triangle(width-height/20, 0, width, height/20, width, 0);
  stroke(1);
}

void displayGrid()
{
  stroke(1);
  strokeWeight(1);
  for(int i = 1; i < worldWidth; ++i)
  {
    line(i*blockX, 0, i*blockX, worldHeight*blockX);
  }
  for(int i = 1; i < worldHeight; ++i)
  {
    line(0, i*blockY, worldWidth*blockY, i*blockY);
  }
}

void displayBlockChoice()
{
  background(skyColor);
  int maxH = (height-editBlockY)/editBlockY;
  int maxW = (blocks.length/maxH);
  noStroke();
  fill(255);
  rect(width/11, height/11, width*9/11, height*9/11);
  fill(0);
  textSize((height*2/15-height/11)*2);
  text("Choose block", width/2, height*2/15);
  for(int i = 0; i < maxW+1; ++i)
  {
    for(int j = 0; j < maxH; ++j)
    {
      image(blocks[i*maxH+j], width-editBlockX-editBlockX*i, editBlockY+editBlockY*j, editBlockX, editBlockY);
      if(i*maxH+j+1 == blocks.length)
      {
        break;
      }
    }
  }
  image(plrBlocks[10], width/11, height*10/11-playerH);
  fill(255, 0, 0);
  strokeWeight(1);
  rect(width/11+blockX, height*10/11-blockY, editBlockX, editBlockY);
}

void pickBlock()
{
  int maxH = (height-editBlockY)/editBlockY;
  int maxW = (blocks.length/maxH)+1;
  if(mousePressed && mouseX >= width-editBlockX*maxW && mouseX <= width && mouseY >= editBlockY && (width-mouseX)/editBlockX*maxH+(mouseY-editBlockY)/editBlockY < blocks.length)
  {
    blockChosen = (width-mouseX)/editBlockX*maxH+(mouseY-editBlockY)/editBlockY;
    spawnpointRelocation = false;
  }
  else if(mousePressed && mouseX >= width/11 && mouseX < width/11+playerW && mouseY >= height*10/11-playerH && mouseY < height*10/11)
  {
    blockChosen = -1;
    spawnpointRelocation = true;
  }
  else if(mousePressed && mouseX >= width/11+blockX && mouseX < width/11+2*blockX && mouseY >= height*10/11-blockY && mouseY < height*10/11)
  {
    blockChosen = -1;
    spawnpointRelocation = false;
  }
}

void displayChosen()
{
  if(choosingBlock)
  {
    if(blockChosen > -1)
      image(blocks[blockChosen], mouseX, mouseY, blockX*2, blockY*2);
    else if(spawnpointRelocation)
    {
      image(plrBlocks[10], mouseX, mouseY, playerW*2, playerH*2);
    }
    else if(blockChosen == -1)
    {
      fill(255, 0, 0);
      noStroke();
      rect(mouseX, mouseY, blockX, blockY);
    }
  }
  else
  {
    if(blockChosen > -1)
      image(blocks[blockChosen], mouseX-editorX, mouseY-editorY, blockX*2, blockY*2);
    else if(spawnpointRelocation)
    {
      image(plrBlocks[10], mouseX-editorX, mouseY-editorY, playerW*2, playerH*2);
    }
    else if(blockChosen == -1)
    {
      fill(255, 0, 0);
      noStroke();
      rect(mouseX-editorX, mouseY-editorY, blockX, blockY);
    }
  }
}

void changeBlocks()
{
  if(!mousePressed || pressedButton || mouseX+height/20-width-mouseY > 0)
    return;
  if(spawnpointRelocation)
  {
    playerSpawn[whichWorld][0] = int((mouseX-editorX)/blockX)*blockX;
    playerSpawn[whichWorld][1] = int((mouseY-editorY)/blockY)*blockY;
  }
  else if(blockChosen == blocks.length-1)
  {
    int dX = doorCoords[whichWorld][0];
    int dY = doorCoords[whichWorld][1];
    doorCoords[whichWorld][0] = int((mouseX-editorX)/blockX);
    doorCoords[whichWorld][1] = int((mouseY-editorY)/blockY);
    grid[whichWorld][dY][dX] = -1;
    grid[whichWorld][doorCoords[whichWorld][1]][doorCoords[whichWorld][0]] = blockChosen;
  }
  else
  {
    grid[whichWorld][(mouseY-editorY)/blockY][(mouseX-editorX)/blockX] = blockChosen;
  }
}
  

int[][] initWorld(int rows, int columns)
{
  int[][] world;
  world = new int[rows][columns];
  for(int i = 0; i < rows; ++i)
  {
    for(int j = 0; j < columns; ++j)
    {
      world[i][j] = -1;
    }
  }
  return world;
}

void initWorlds()
{
  for(int i = 0; i < nrOfLevels; ++i)
  {
    grid[i] = initWorld(worldHeight, worldWidth);
    writeOutWorld(grid[i],i);
  }
}

void writeOutWorld(int[][] world, int worldID)
{
  PrintWriter textFile;
  textFile = createWriter("world_"+worldID+".txt");
  textFile.print(playerSpawn[worldID][0]+" "+playerSpawn[worldID][1]+"\n");
  for(int i = 0; i < worldHeight; ++i)
  {
    for(int j = 0; j < worldWidth; ++j)
    {
      textFile.print(world[i][j]);
      if(j != worldWidth-1)
        textFile.print(" ");
    }
    if(i != worldHeight-1)
      textFile.print("\n");
  }
  textFile.flush();
  textFile.close();
}

void loadTxtToWorld(int worldID)
{
  String temp;
  txtFileLines = loadStrings("world_"+worldID+".txt");
  int tempInt = 0;
  while(txtFileLines[0].charAt(tempInt) != ' ')
  {
    tempInt++;
  }
  temp = txtFileLines[0].substring(0, tempInt);
  playerSpawn[worldID][0] = Integer.valueOf(temp);
  temp = txtFileLines[0].substring(tempInt+1);
  playerSpawn[worldID][1] = Integer.valueOf(temp);
  for(int j = 0; j < worldHeight; ++j)
  {
    for(int y = 0; y < worldWidth; ++y)
    {
      int a = 2;
      temp = "";
      if(txtFileLines[j+1].length() == 1)
      {
        grid[worldID][j][0] = Integer.valueOf(txtFileLines[j+1]);
        continue;
      }
      while(temp.length() == 0 || temp.charAt(0) != ' ' && txtFileLines[j+1].length()+1 != a)
      {
        temp = txtFileLines[j+1].substring(txtFileLines[j+1].length()-a);
        ++a;
      }
      if(temp.charAt(0) == ' ') temp = temp.substring(1);
      a--;
      txtFileLines[j+1] = txtFileLines[j+1].substring(0, txtFileLines[j+1].length()-a);
      grid[worldID][j][worldWidth-y-1] = Integer.valueOf(temp);
      if(grid[worldID][j][worldWidth-y-1] == blocks.length-1)
      {
        doorCoords[worldID][0] = worldWidth-y-1;
        doorCoords[worldID][1] = j;
        isDoor[worldID] = true;
      }
    }
  }
  if(!isDoor[worldID])
  {
    isDoor[worldID] = true;
    doorCoords[worldID][0] = 0;
    doorCoords[worldID][1] = worldHeight-1;
    grid[worldID][doorCoords[worldID][1]][doorCoords[worldID][0]] = blocks.length-1;
  }
}

void cameraView()
{
  if(!editor)
  {
    if(playerX-width/2 >= 0 && playerX-width/2 <= worldWidth*blockX-width)
    {
      translX = -playerX+width/2;
      editorX = translX;
    }
    if(playerY-height/2 >= 0 && playerY-height/2 <= worldHeight*blockY-height)
    {
      translY = -playerY+height/2;
      editorY = translY;
    }
    translate(translX, translY);
  }
  else
  {
    if(keyPressed)
    {
      switch(keyCode)
      {
        case RIGHT:
          if(editorX-10 >= -worldWidth*blockX+width)
            editorX -= 10;
          else
            editorX = -worldWidth*blockX+width;
          break;
        case LEFT:
          if(editorX+10 <= 0)
            editorX += 10;
          else
            editorX = 0;
          break;
        case UP:
          if(editorY+10 <= 0)
            editorY += 10;
          else
            editorY = 0;
          break;
        case DOWN:
          if(editorY-10 >= -worldHeight*blockY+height)
            editorY -= 10;
          else
            editorY = -worldHeight*blockY+height;
          break;
      }
    }
    translate(editorX, editorY);
  }
}

void displayWorld()
{
  background(skyColor);
  cameraView();
  for(int i = 0; i < worldHeight; ++i)
  {
    for(int j = 0; j < worldWidth; ++j)
    {
      if(grid[whichWorld][i][j] > -1) image(blocks[grid[whichWorld][i][j]], j*blockX, i*blockY, blockX, blockY);
    }
  }
}

boolean isSolid(int x, int y)
{
  for(int i = 0; i < solidsID.length; ++i)
  {
    if(solidsID[i] == grid[whichWorld][y][x])
      return true;
  }
  return false;
}

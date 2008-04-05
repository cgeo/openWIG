require "Wherigo"
ZonePoint = Wherigo.ZonePoint
Distance = Wherigo.Distance
Player = Wherigo.Player

-- #Author Directives Go Here# --
-- #End Author Directives# --

-- lua functions
function empty (self, a)
end
function Wherigo.ZMedia(a)
	return {}
end
--function Wherigo.Zone(a)
--	return {}
--end
--function Wherigo.ZCharacter(a)
--	return {MoveTo = empty}
--end
--function Wherigo.ZCommand(a)
--	return a
--end
--function Wherigo.ZItem(a)
--	return {}
--end
function Wherigo.ZTask(a)
	return {}
end


cartZooventureLevelTwo = Wherigo.ZCartridge()

-- MessageBox Callback Functions Table used by the Builder --
cartZooventureLevelTwo.MsgBoxCBFuncs = {}

zmediabrianpenguin = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediabrianpenguin.Name="brianpenguin"
zmediabrianpenguin.Description=""
zmediabrianpenguin.AltText=""
zmediabrianpenguin.Id="d580fa7e-9dcb-4a08-96a3-c89cea329d9b"
zmediabrianpenguin.Resources = {
{ Type = "jpg", Filename = "brianpenguin.jpg", Directives = {},},
}
zmediabrianpenguinmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediabrianpenguinmsg.Name="brianpenguinmsg"
zmediabrianpenguinmsg.Description=""
zmediabrianpenguinmsg.AltText=""
zmediabrianpenguinmsg.Id="cf8753bd-510c-42b1-9648-82cc74dd8d2b"
zmediabrianpenguinmsg.Resources = {
{ Type = "jpg", Filename = "brianpenguinmsg.jpg", Directives = {},},
}
zmediacolobus = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediacolobus.Name="colobus"
zmediacolobus.Description=""
zmediacolobus.AltText=""
zmediacolobus.Id="b7899886-502a-490f-b3a2-37c05b1a6cb5"
zmediacolobus.Resources = {
{ Type = "jpg", Filename = "colobus.jpg", Directives = {},},
}
zmediacolobusmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediacolobusmsg.Name="colobusmsg"
zmediacolobusmsg.Description=""
zmediacolobusmsg.AltText=""
zmediacolobusmsg.Id="09f327bb-f5a6-4924-9f9a-5738e738f1e0"
zmediacolobusmsg.Resources = {
{ Type = "jpg", Filename = "colobusmsg.jpg", Directives = {},},
}
zmediahen = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediahen.Name="hen"
zmediahen.Description=""
zmediahen.AltText=""
zmediahen.Id="d9fbb21a-d28b-4d29-8025-0d0aa713fe8b"
zmediahen.Resources = {
{ Type = "jpg", Filename = "hen.jpg", Directives = {},},
}
zmediahenmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediahenmsg.Name="henmsg"
zmediahenmsg.Description=""
zmediahenmsg.AltText=""
zmediahenmsg.Id="4bff4a69-29b7-4ed4-bfbe-3e9d429b5107"
zmediahenmsg.Resources = {
{ Type = "jpg", Filename = "henmsg.jpg", Directives = {},},
}
zmediajaredpenguin = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediajaredpenguin.Name="jaredpenguin"
zmediajaredpenguin.Description=""
zmediajaredpenguin.AltText=""
zmediajaredpenguin.Id="ca1548d7-0a73-481a-8065-e7f0d7d7b8c7"
zmediajaredpenguin.Resources = {
{ Type = "jpg", Filename = "jaredpenguin.jpg", Directives = {},},
}
zmediajaredpenguinmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediajaredpenguinmsg.Name="jaredpenguinmsg"
zmediajaredpenguinmsg.Description=""
zmediajaredpenguinmsg.AltText=""
zmediajaredpenguinmsg.Id="5075c78d-f9c0-49bb-8bb4-5a1256c52627"
zmediajaredpenguinmsg.Resources = {
{ Type = "jpg", Filename = "jaredpenguinmsg.jpg", Directives = {},},
}
zmediapenguingroup = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediapenguingroup.Name="penguingroup"
zmediapenguingroup.Description=""
zmediapenguingroup.AltText=""
zmediapenguingroup.Id="0c8bc089-e148-4c01-9594-ec5443493819"
zmediapenguingroup.Resources = {
{ Type = "jpg", Filename = "penguingroup.jpg", Directives = {},},
}
zmediapenguingroupmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediapenguingroupmsg.Name="penguingroupmsg"
zmediapenguingroupmsg.Description=""
zmediapenguingroupmsg.AltText=""
zmediapenguingroupmsg.Id="0de5bf5f-8d0d-4ed9-8f30-82ca3aa0cc37"
zmediapenguingroupmsg.Resources = {
{ Type = "jpg", Filename = "penguingroupmsg.jpg", Directives = {},},
}
zmediapinkbackgorilla = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediapinkbackgorilla.Name="pinkbackgorilla"
zmediapinkbackgorilla.Description=""
zmediapinkbackgorilla.AltText=""
zmediapinkbackgorilla.Id="542f438e-c32a-41a4-804b-ce9e76ea9d64"
zmediapinkbackgorilla.Resources = {
{ Type = "jpg", Filename = "pinkbackgorilla.jpg", Directives = {},},
}
zmediapinkbackgorillamsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediapinkbackgorillamsg.Name="pinkbackgorillamsg"
zmediapinkbackgorillamsg.Description=""
zmediapinkbackgorillamsg.AltText=""
zmediapinkbackgorillamsg.Id="e802c7a9-927d-4b04-af83-151825bc2589"
zmediapinkbackgorillamsg.Resources = {
{ Type = "jpg", Filename = "pinkbackgorillamsg.jpg", Directives = {},},
}
zmediasadpig = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediasadpig.Name="sadpig"
zmediasadpig.Description=""
zmediasadpig.AltText=""
zmediasadpig.Id="63baa4ea-8489-4ac2-8c18-aa4951a75d39"
zmediasadpig.Resources = {
{ Type = "jpg", Filename = "sadpig.jpg", Directives = {},},
}
zmediasadpigmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediasadpigmsg.Name="sadpigmsg"
zmediasadpigmsg.Description=""
zmediasadpigmsg.AltText=""
zmediasadpigmsg.Id="39776fc5-943a-4e0f-a489-5290d120246b"
zmediasadpigmsg.Resources = {
{ Type = "jpg", Filename = "sadpigmsg.jpg", Directives = {},},
}
zmediazonepenguinpool = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediazonepenguinpool.Name="zonepenguinpool"
zmediazonepenguinpool.Description=""
zmediazonepenguinpool.AltText=""
zmediazonepenguinpool.Id="57a906a2-b8ab-4016-8577-75e295abe3cb"
zmediazonepenguinpool.Resources = {
{ Type = "jpg", Filename = "zonepenguinpool.jpg", Directives = {},},
}
zmediazonerainforest = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediazonerainforest.Name="zonerainforest"
zmediazonerainforest.Description=""
zmediazonerainforest.AltText=""
zmediazonerainforest.Id="d3197f53-5b77-4a6b-a889-6efc4ef6d8c2"
zmediazonerainforest.Resources = {
{ Type = "jpg", Filename = "zonerainforest.jpg", Directives = {},},
}
zmediaegg2msg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediaegg2msg.Name="egg2msg"
zmediaegg2msg.Description=""
zmediaegg2msg.AltText=""
zmediaegg2msg.Id="edb05e99-7a56-411e-b177-40c79bf1bf3a"
zmediaegg2msg.Resources = {
{ Type = "jpg", Filename = "egg2msg.jpg", Directives = {},},
}
zmediazonetemperate = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediazonetemperate.Name="zonetemperate"
zmediazonetemperate.Description=""
zmediazonetemperate.AltText=""
zmediazonetemperate.Id="8872ac35-20e7-4f77-a682-f803458e0216"
zmediazonetemperate.Resources = {
{ Type = "jpg", Filename = "zonetemperate.jpg", Directives = {},},
}
zmediaegg2 = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediaegg2.Name="egg2"
zmediaegg2.Description=""
zmediaegg2.AltText=""
zmediaegg2.Id="d507974d-2372-45f2-a23d-6e6f7f6214c3"
zmediaegg2.Resources = {
{ Type = "jpg", Filename = "egg2.jpg", Directives = {},},
}
zmediaegg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediaegg.Name="egg"
zmediaegg.Description=""
zmediaegg.AltText=""
zmediaegg.Id="20106529-f335-414b-8493-1bb106d699eb"
zmediaegg.Resources = {
{ Type = "jpg", Filename = "egg.jpg", Directives = {},},
}
zmediaeggmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediaeggmsg.Name="eggmsg"
zmediaeggmsg.Description=""
zmediaeggmsg.AltText=""
zmediaeggmsg.Id="d217c844-65d2-4aa0-8921-38ebba6d1dc9"
zmediaeggmsg.Resources = {
{ Type = "jpg", Filename = "eggmsg.jpg", Directives = {},},
}
zmediazookeepermsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediazookeepermsg.Name="zookeepermsg"
zmediazookeepermsg.Description=""
zmediazookeepermsg.AltText=""
zmediazookeepermsg.Id="7fad647b-cc61-494f-91cd-870c3cfb8421"
zmediazookeepermsg.Resources = {
{ Type = "jpg", Filename = "zookeepermsg.jpg", Directives = {},},
}
zmediazookeeper = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediazookeeper.Name="zookeeper"
zmediazookeeper.Description=""
zmediazookeeper.AltText=""
zmediazookeeper.Id="d5646a25-47d3-48bd-95f2-4882706fc1c7"
zmediazookeeper.Resources = {
{ Type = "jpg", Filename = "zookeeper.jpg", Directives = {},},
}
zmedialemon = Wherigo.ZMedia(cartZooventureLevelTwo)
zmedialemon.Name="lemon"
zmedialemon.Description=""
zmedialemon.AltText=""
zmedialemon.Id="f3137d01-a92c-4321-b03c-af538e06d7b6"
zmedialemon.Resources = {
{ Type = "jpg", Filename = "lemon.jpg", Directives = {},},
}
zmedialemonmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmedialemonmsg.Name="lemonmsg"
zmedialemonmsg.Description=""
zmedialemonmsg.AltText=""
zmedialemonmsg.Id="469733b0-c928-4110-a821-ce633d2473af"
zmedialemonmsg.Resources = {
{ Type = "jpg", Filename = "lemonmsg.jpg", Directives = {},},
}
zmediashampoo = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediashampoo.Name="shampoo"
zmediashampoo.Description=""
zmediashampoo.AltText=""
zmediashampoo.Id="d87f6c99-fd0d-472f-b21f-28da217a5e60"
zmediashampoo.Resources = {
{ Type = "jpg", Filename = "shampoo.jpg", Directives = {},},
}
zmediashampoomsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediashampoomsg.Name="shampoomsg"
zmediashampoomsg.Description=""
zmediashampoomsg.AltText=""
zmediashampoomsg.Id="edbba81c-dc40-427f-b41b-8e291e051e88"
zmediashampoomsg.Resources = {
{ Type = "jpg", Filename = "shampoomsg.jpg", Directives = {},},
}
zmediahappypig = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediahappypig.Name="happypig"
zmediahappypig.Description=""
zmediahappypig.AltText=""
zmediahappypig.Id="0c00bb76-0c89-459f-aeaa-223a9fe91b2a"
zmediahappypig.Resources = {
{ Type = "jpg", Filename = "happypig.jpg", Directives = {},},
}
zmediahappypigmsg = Wherigo.ZMedia(cartZooventureLevelTwo)
zmediahappypigmsg.Name="happypigmsg"
zmediahappypigmsg.Description=""
zmediahappypigmsg.AltText=""
zmediahappypigmsg.Id="91d2ed87-7e6d-4358-8c7c-fc1f18fd316e"
zmediahappypigmsg.Resources = {
{ Type = "jpg", Filename = "happypigmsg.jpg", Directives = {},},
}
-- Cartridge Info --
cartZooventureLevelTwo.Id="2cdbb73d-2236-49d4-928a-873a80266fe4"
cartZooventureLevelTwo.Name="Zooventure Level Two"
cartZooventureLevelTwo.Description=[[What's going on at the Zoo? Something seems fishy. Are the penguins making trouble?]]
cartZooventureLevelTwo.Visible=true
cartZooventureLevelTwo.Activity="TourGuide"
cartZooventureLevelTwo.StartingLocationDescription=[[]]
cartZooventureLevelTwo.StartingLocation = ZonePoint(47.666489,-122.350749,0)
cartZooventureLevelTwo.Version=""
cartZooventureLevelTwo.Company=""
cartZooventureLevelTwo.Author=""
cartZooventureLevelTwo.BuilderVersion="2.0.4907.3996"
cartZooventureLevelTwo.CreateDate="1/12/2008 5:33:36 PM"
cartZooventureLevelTwo.PublishDate="1/1/0001 12:00:00 AM"
cartZooventureLevelTwo.UpdateDate="3/21/2008 3:31:00 PM"
cartZooventureLevelTwo.LastPlayedDate="1/1/0001 12:00:00 AM"
cartZooventureLevelTwo.TargetDevice="PocketPC"
cartZooventureLevelTwo.TargetDeviceVersion="0"
cartZooventureLevelTwo.StateId="1"
cartZooventureLevelTwo.CountryId="2"
cartZooventureLevelTwo.Complete=false
cartZooventureLevelTwo.UseLogging=false
cartZooventureLevelTwo.Media=zmediapenguingroup

-- Zones --
zonePenguinPool = Wherigo.Zone(cartZooventureLevelTwo)
zonePenguinPool.Id="6858a125-5ef1-4267-a916-e552397d65ac"
zonePenguinPool.Name="Penguin Pool"
zonePenguinPool.Description=[[]]
zonePenguinPool.Visible=false
zonePenguinPool.DistanceRange = Distance(-1, "feet")
zonePenguinPool.ShowObjects="OnEnter"
zonePenguinPool.ProximityRange = Distance(200, "feet")
zonePenguinPool.AllowSetPositionTo=false
zonePenguinPool.Active=false
zonePenguinPool.Points = {
  ZonePoint(47.66877,-122.3528,0),
  ZonePoint(47.66863,-122.35271,0),
  ZonePoint(47.66862,-122.3524,0),
  ZonePoint(47.66865,-122.35206,0),
  ZonePoint(47.66883,-122.35193,0),
  ZonePoint(47.66895,-122.35195,0),
  ZonePoint(47.66907,-122.35218,0),
  ZonePoint(47.66911,-122.35248,0),
  ZonePoint(47.66899,-122.35268,0),
  ZonePoint(47.66887,-122.35275,0)
}
zonePenguinPool.OriginalPoint = ZonePoint(47.66877,-122.3528,0)
zonePenguinPool.DistanceRangeUOM = "Feet"
zonePenguinPool.ProximityRangeUOM = "Feet"
zonePenguinPool.OutOfRangeName = ""
zonePenguinPool.InRangeName = ""
zonePenguinPool.Media=zmediazonepenguinpool

zoneTemperateForest = Wherigo.Zone(cartZooventureLevelTwo)
zoneTemperateForest.Id="7ea4f27a-4301-473a-b5e6-3489a3d58754"
zoneTemperateForest.Name="Temperate Forest"
zoneTemperateForest.Description=[[]]
zoneTemperateForest.Visible=false
zoneTemperateForest.DistanceRange = Distance(-1, "feet")
zoneTemperateForest.ShowObjects="OnEnter"
zoneTemperateForest.ProximityRange = Distance(200, "feet")
zoneTemperateForest.AllowSetPositionTo=false
zoneTemperateForest.Active=false
zoneTemperateForest.Points = {
  ZonePoint(47.66661,-122.35085,0),
  ZonePoint(47.66671,-122.35106,0),
  ZonePoint(47.66683,-122.35127,0),
  ZonePoint(47.66682,-122.35165,0),
  ZonePoint(47.66681,-122.352,0),
  ZonePoint(47.66672,-122.35217,0),
  ZonePoint(47.66658,-122.35228,0),
  ZonePoint(47.6664,-122.35235,0),
  ZonePoint(47.66624,-122.35224,0),
  ZonePoint(47.66612,-122.35205,0),
  ZonePoint(47.66601,-122.35148,0),
  ZonePoint(47.66604,-122.35122,0),
  ZonePoint(47.66614,-122.35099,0),
  ZonePoint(47.6662,-122.35074,0),
  ZonePoint(47.66638,-122.35074,0)
}
zoneTemperateForest.OriginalPoint = ZonePoint(47.66661,-122.35085,0)
zoneTemperateForest.DistanceRangeUOM = "Feet"
zoneTemperateForest.ProximityRangeUOM = "Feet"
zoneTemperateForest.OutOfRangeName = ""
zoneTemperateForest.InRangeName = ""
zoneTemperateForest.Media=zmediazonetemperate

zoneTropicalRainForest = Wherigo.Zone(cartZooventureLevelTwo)
zoneTropicalRainForest.Id="3a8b0d67-4d41-45fe-b714-3e26e626d32a"
zoneTropicalRainForest.Name="Tropical Rain Forest"
zoneTropicalRainForest.Description=[[]]
zoneTropicalRainForest.Visible=false
zoneTropicalRainForest.DistanceRange = Distance(1500, "feet")
zoneTropicalRainForest.ShowObjects="OnEnter"
zoneTropicalRainForest.ProximityRange = Distance(200, "feet")
zoneTropicalRainForest.AllowSetPositionTo=false
zoneTropicalRainForest.Active=false
zoneTropicalRainForest.Points = {
  ZonePoint(47.6685,-122.35279,0),
  ZonePoint(47.66851,-122.35257,0),
  ZonePoint(47.66852,-122.35234,0),
  ZonePoint(47.66858,-122.35217,0),
  ZonePoint(47.66857,-122.35197,0),
  ZonePoint(47.66854,-122.35171,0),
  ZonePoint(47.6685,-122.35148,0),
  ZonePoint(47.66833,-122.35127,0),
  ZonePoint(47.66811,-122.35114,0),
  ZonePoint(47.6679,-122.35123,0),
  ZonePoint(47.66771,-122.35134,0),
  ZonePoint(47.66755,-122.35144,0),
  ZonePoint(47.66742,-122.35169,0),
  ZonePoint(47.66739,-122.35206,0),
  ZonePoint(47.66745,-122.3523,0),
  ZonePoint(47.66762,-122.35251,0),
  ZonePoint(47.66776,-122.35271,0),
  ZonePoint(47.66795,-122.3527,0),
  ZonePoint(47.66812,-122.35259,0),
  ZonePoint(47.66826,-122.35261,0),
  ZonePoint(47.66837,-122.35284,0)
}
zoneTropicalRainForest.OriginalPoint = ZonePoint(47.6685,-122.35279,0)
zoneTropicalRainForest.DistanceRangeUOM = "Feet"
zoneTropicalRainForest.ProximityRangeUOM = "Feet"
zoneTropicalRainForest.OutOfRangeName = ""
zoneTropicalRainForest.InRangeName = ""
zoneTropicalRainForest.Media=zmediazonerainforest

-- Characters --
zcharacterZookeeper = Wherigo.ZCharacter(cartZooventureLevelTwo)
zcharacterZookeeper.Id="a872b024-ece7-4af7-a4bd-16b916061f60"
zcharacterZookeeper.Name="Zookeeper"
zcharacterZookeeper.Description=[[]]
zcharacterZookeeper.Visible=false
zcharacterZookeeper.Media=zmediazookeeper
zcharacterZookeeper.Gender="Male"
zcharacterZookeeper.Type="NPC"
zcharacterZookeeper.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zcharacterZookeeper.Commands = {
  Talk = Wherigo.ZCommand{Text="Talk", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zcharacterZookeeper.Commands.Talk.Custom = true
zcharacterZookeeper.Commands.Talk.Id="9cd46cc0-a4e4-47dc-93ee-9f5d11088ef5"
zcharacterZookeeper.Commands.Talk.WorksWithAll = true

zcharacterChicken = Wherigo.ZCharacter(cartZooventureLevelTwo)
zcharacterChicken.Id="1d4b59f2-2141-45b1-bfb8-b053aa361978"
zcharacterChicken.Name="Chicken"
zcharacterChicken.Description=[[]]
zcharacterChicken.Visible=false
zcharacterChicken.Media=zmediahen
zcharacterChicken.Gender="Female"
zcharacterChicken.Type="NPC"
zcharacterChicken.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zcharacterChicken.Commands = {
  Talk = Wherigo.ZCommand{Text="Talk", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zcharacterChicken.Commands.Talk.Custom = true
zcharacterChicken.Commands.Talk.Id="e5cdd029-d0d2-4e06-a0ec-4a1c0088f4f5"
zcharacterChicken.Commands.Talk.WorksWithAll = true
zcharacterChicken.Commands.Examine.Custom = true
zcharacterChicken.Commands.Examine.Id="2d8f99be-de4f-426b-a65d-99a3038ba7ff"
zcharacterChicken.Commands.Examine.WorksWithAll = true

zcharacterPig = Wherigo.ZCharacter(cartZooventureLevelTwo)
zcharacterPig.Id="6017dd2d-59ed-4936-a86e-e8a91ce79868"
zcharacterPig.Name="Pig"
zcharacterPig.Description=[[]]
zcharacterPig.Visible=false
zcharacterPig.Media=zmediasadpig
zcharacterPig.Gender="Male"
zcharacterPig.Type="NPC"
zcharacterPig.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zcharacterPig.Commands = {
  Talk = Wherigo.ZCommand{Text="Talk", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zcharacterPig.Commands.Talk.Custom = true
zcharacterPig.Commands.Talk.Id="546b8459-5a9f-4aa9-a182-275a22b8ba47"
zcharacterPig.Commands.Talk.WorksWithAll = true
zcharacterPig.Commands.Examine.Custom = true
zcharacterPig.Commands.Examine.Id="1785d7a1-6207-4309-b535-97b0a435563c"
zcharacterPig.Commands.Examine.WorksWithAll = true

zcharacterGorilla = Wherigo.ZCharacter(cartZooventureLevelTwo)
zcharacterGorilla.Id="bba9357a-d2a1-408b-b222-3b4cd1a5a180"
zcharacterGorilla.Name="Gorilla"
zcharacterGorilla.Description=[[]]
zcharacterGorilla.Visible=false
zcharacterGorilla.Media=zmediapinkbackgorilla
zcharacterGorilla.Gender="Male"
zcharacterGorilla.Type="NPC"
zcharacterGorilla.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zcharacterGorilla.Commands = {
  Talk = Wherigo.ZCommand{Text="Talk", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zcharacterGorilla.Commands.Talk.Custom = true
zcharacterGorilla.Commands.Talk.Id="1d5ff1e0-49f7-4c6b-b928-b254b2852031"
zcharacterGorilla.Commands.Talk.WorksWithAll = true
zcharacterGorilla.Commands.Examine.Custom = true
zcharacterGorilla.Commands.Examine.Id="7e66aa05-9e89-48e8-9ac9-fd72a0f59b49"
zcharacterGorilla.Commands.Examine.WorksWithAll = true

zcharacterColobus = Wherigo.ZCharacter(cartZooventureLevelTwo)
zcharacterColobus.Id="b94ecd48-167f-46df-a195-a0ed41ac12dd"
zcharacterColobus.Name="Colobus"
zcharacterColobus.Description=[[]]
zcharacterColobus.Visible=false
zcharacterColobus.Media=zmediacolobus
zcharacterColobus.Gender="Male"
zcharacterColobus.Type="NPC"
zcharacterColobus.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zcharacterColobus.Commands = {
  Talk = Wherigo.ZCommand{Text="Talk", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zcharacterColobus.Commands.Talk.Custom = true
zcharacterColobus.Commands.Talk.Id="e4980769-7098-482a-bacf-eea3283838d3"
zcharacterColobus.Commands.Talk.WorksWithAll = true
zcharacterColobus.Commands.Examine.Custom = true
zcharacterColobus.Commands.Examine.Id="4f8420d3-61b9-425b-ae32-c939b6c50c01"
zcharacterColobus.Commands.Examine.WorksWithAll = true

-- Items --
zitemYellowEgg = Wherigo.ZItem(cartZooventureLevelTwo)
zitemYellowEgg.Id="179d36bf-c10d-4b5b-86be-3bc17f6b249f"
zitemYellowEgg.Name="Yellow Egg"
zitemYellowEgg.Description=[[Sweet and sour. Nature's Candy.]]
zitemYellowEgg.Visible=false
zitemYellowEgg.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zitemYellowEgg.Media=zmedialemon
zitemYellowEgg.Locked = false
zitemYellowEgg.Opened = false
zitemYellowEgg.Commands = {
  Take = Wherigo.ZCommand{Text="Take", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Give = Wherigo.ZCommand{Text="Give", CmdWith=true, Enabled=true, EmptyTargetListText="Nothing available"},
}
zitemYellowEgg.Commands.Take.Custom = true
zitemYellowEgg.Commands.Take.Id="e72d3723-f2b0-4955-9eaf-5b5e538fcea2"
zitemYellowEgg.Commands.Take.WorksWithAll = true
zitemYellowEgg.Commands.Examine.Custom = true
zitemYellowEgg.Commands.Examine.Id="07ae969b-8fb5-4f44-ae3c-7027b943e834"
zitemYellowEgg.Commands.Examine.WorksWithAll = true
zitemYellowEgg.Commands.Give.Custom = true
zitemYellowEgg.Commands.Give.Id="61b89f90-aeda-4224-8554-fe15b9fb67d1"
zitemYellowEgg.Commands.Give.WorksWithAll = false
zitemYellowEgg.Commands.Give.WorksWithListIds = {"1d4b59f2-2141-45b1-bfb8-b053aa361978", "6017dd2d-59ed-4936-a86e-e8a91ce79868", }

zitemEgg = Wherigo.ZItem(cartZooventureLevelTwo)
zitemEgg.Id="6ddde4f1-1b2e-472b-afa3-70829d888aad"
zitemEgg.Name="Egg"
zitemEgg.Description=[[A Chicken's egg]]
zitemEgg.Visible=false
zitemEgg.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zitemEgg.Media=zmediaegg
zitemEgg.Locked = false
zitemEgg.Opened = false
zitemEgg.Commands = {
  Take = Wherigo.ZCommand{Text="Take", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zitemEgg.Commands.Take.Custom = true
zitemEgg.Commands.Take.Id="9a95d209-cff5-49f8-960e-a5e7e45c7982"
zitemEgg.Commands.Take.WorksWithAll = true
zitemEgg.Commands.Examine.Custom = true
zitemEgg.Commands.Examine.Id="2c8da68d-5337-4928-9ad6-ff3d2ad11eb8"
zitemEgg.Commands.Examine.WorksWithAll = true

zitemShampoo = Wherigo.ZItem(cartZooventureLevelTwo)
zitemShampoo.Id="26df0332-3ab4-4705-850f-9933e8e0206d"
zitemShampoo.Name="Shampoo"
zitemShampoo.Description=[[]]
zitemShampoo.Visible=false
zitemShampoo.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zitemShampoo.Media=zmediashampoo
zitemShampoo.Locked = false
zitemShampoo.Opened = false
zitemShampoo.Commands = {
}

zitemEgg2 = Wherigo.ZItem(cartZooventureLevelTwo)
zitemEgg2.Id="524e40aa-f088-49b7-9e33-73e7ef4974ca"
zitemEgg2.Name="Egg2"
zitemEgg2.Description=[[A Chicken's egg]]
zitemEgg2.Visible=false
zitemEgg2.ObjectLocation = Wherigo.INVALID_ZONEPOINT
zitemEgg2.Media=zmediaegg2
zitemEgg2.Locked = false
zitemEgg2.Opened = false
zitemEgg2.Commands = {
  Take = Wherigo.ZCommand{Text="Take", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
  Examine = Wherigo.ZCommand{Text="Examine", CmdWith=false, Enabled=true, EmptyTargetListText="Nothing available"},
}
zitemEgg2.Commands.Take.Custom = true
zitemEgg2.Commands.Take.Id="509c67c6-65ae-4633-9a6d-c42462b89df8"
zitemEgg2.Commands.Take.WorksWithAll = true
zitemEgg2.Commands.Examine.Custom = true
zitemEgg2.Commands.Examine.Id="c6413c7f-95b1-4101-993c-3adb1dc12296"
zitemEgg2.Commands.Examine.WorksWithAll = true

-- Tasks --
ztaskCurlthePigstail = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskCurlthePigstail.Id="dff52aa0-8c06-47aa-b18f-10f83c9f1dd4"
ztaskCurlthePigstail.Name="Curl the Pig's tail "
ztaskCurlthePigstail.Description=[[The Pig's tail is completely straightened! You need to make it curly again and save him from certain humiliation.]]
ztaskCurlthePigstail.Visible=true
ztaskCurlthePigstail.Media=zmediasadpig
ztaskCurlthePigstail.Active=false
ztaskCurlthePigstail.Complete=false
ztaskCurlthePigstail.CorrectState = "None"

ztaskGototheTemperateForest = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskGototheTemperateForest.Id="37717a00-935d-43d0-90ba-9a1231b0fb91"
ztaskGototheTemperateForest.Name="Go to the Temperate Forest"
ztaskGototheTemperateForest.Description=[[Head over to the Temperate Forest to see what's going on with the animals.]]
ztaskGototheTemperateForest.Visible=true
ztaskGototheTemperateForest.Media=zmediazonetemperate
ztaskGototheTemperateForest.Active=false
ztaskGototheTemperateForest.Complete=false
ztaskGototheTemperateForest.CorrectState = "None"

ztaskGototheTropicalRainForest = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskGototheTropicalRainForest.Id="ead40b60-29bf-435d-9934-a07264760782"
ztaskGototheTropicalRainForest.Name="Go to the Tropical Rain Forest"
ztaskGototheTropicalRainForest.Description=[[Find out what's happening at the Tropical Rain Forest.]]
ztaskGototheTropicalRainForest.Visible=true
ztaskGototheTropicalRainForest.Media=zmediazonerainforest
ztaskGototheTropicalRainForest.Active=false
ztaskGototheTropicalRainForest.Complete=false
ztaskGototheTropicalRainForest.CorrectState = "None"

ztaskFindJaredthePenguin = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskFindJaredthePenguin.Id="8f540264-352f-410f-9fce-a8b0e6a19c8b"
ztaskFindJaredthePenguin.Name="Find Jared the Penguin"
ztaskFindJaredthePenguin.Description=[[Jared the Penguin has been causing trouble everywhere! Don't let him get away.]]
ztaskFindJaredthePenguin.Visible=true
ztaskFindJaredthePenguin.Media=zmediajaredpenguin
ztaskFindJaredthePenguin.Active=false
ztaskFindJaredthePenguin.Complete=false
ztaskFindJaredthePenguin.CorrectState = "None"

ztaskFindBrianthePenguin = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskFindBrianthePenguin.Id="82dca4b1-1e7a-4166-8d58-e343cd5b56c3"
ztaskFindBrianthePenguin.Name="Find Brian the Penguin"
ztaskFindBrianthePenguin.Description=[[Can't let the troublesome penguin get away with this! Find this penguin.]]
ztaskFindBrianthePenguin.Visible=true
ztaskFindBrianthePenguin.Media=zmediabrianpenguin
ztaskFindBrianthePenguin.Active=false
ztaskFindBrianthePenguin.Complete=false
ztaskFindBrianthePenguin.CorrectState = "None"

ztaskGotothePenguinPool = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskGotothePenguinPool.Id="3246d920-67fe-4b66-840e-fcf810ef69ee"
ztaskGotothePenguinPool.Name="Go to the Penguin Pool"
ztaskGotothePenguinPool.Description=[[Check out the Penguin Pool to check on the penguins and to chat with our zookeeper to find out what's next!]]
ztaskGotothePenguinPool.Visible=true
ztaskGotothePenguinPool.Media=zmediazonepenguinpool
ztaskGotothePenguinPool.Active=false
ztaskGotothePenguinPool.Complete=false
ztaskGotothePenguinPool.CorrectState = "None"

ztaskFixGorillashair = Wherigo.ZTask(cartZooventureLevelTwo)
ztaskFixGorillashair.Id="1ae09221-6dfe-4ff6-a500-f3afe8b3e818"
ztaskFixGorillashair.Name="Fix Gorilla's hair"
ztaskFixGorillashair.Description=[[Pink doesn't seem like the right color for Gorilla. ]]
ztaskFixGorillashair.Visible=true
ztaskFixGorillashair.Media=zmediapinkbackgorilla
ztaskFixGorillashair.Active=false
ztaskFixGorillashair.Complete=false
ztaskFixGorillashair.CorrectState = "None"

-- Cartridge Variables --
-- Builder Variables (to be read by the builder only) --
buildervar = {}
-- ZTimers --

-- Inputs --

-- WorksWithList for zobject zcommands --
zitemYellowEgg.Commands.Give.WorksWithList={zcharacterChicken, zcharacterPig, }

--
-- Events/Conditions/Actions --
--

-------------------------------------------------------------------------------
------Builder Generated functions, Do not Edit, this will be overwritten------
-------------------------------------------------------------------------------

function cartZooventureLevelTwo:OnStart()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.Dialog{{Text=[["In level one, we discovered that two sneaky penguins escaped and have been causing trouble around the zoo. We need to talk to the animals even more to uncover the mystery."]],Media=zmediazookeepermsg},{Text=[["Let's head over to the Temperate Forest."]],Media=zmediazookeepermsg},{Text=[[New Task: Go to Temperate Forest.]],Media=zmediazonetemperate},}
zoneTemperateForest.Visible = true
zoneTemperateForest.Active = true
ztaskGototheTemperateForest.Active = true
end

function zoneTemperateForest:OnEnter()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[["In level one, we learned about chicken's odd egg and pig's straight tail. Shall we talk to them?"]],Media=zmediazookeepermsg,}
zcharacterPig:MoveTo(zoneTemperateForest)
zcharacterChicken:MoveTo(zoneTemperateForest)
zcharacterZookeeper:MoveTo(zoneTemperateForest)
zcharacterPig.Visible = true
zcharacterZookeeper.Visible = true
zitemYellowEgg.Commands["Give"].Enabled = false
ztaskGototheTemperateForest.Complete = true
end

function zcharacterPig:OnTalk()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskCurlthePigstail.Active == false then
Wherigo.Dialog{{Text=[["My tail is still straight. How am I supposed go to the barn dance like this? I've only got a few hours to get ready!"]],Media=zmediasadpigmsg},{Text=[["Please. Help me."]],Media=zmediasadpigmsg},{Text=[[First Task: Curl the pig's tail.]],Media=zmediasadpig},}
zcharacterChicken.Visible = true
ztaskCurlthePigstail.Active = true
else
Wherigo.MessageBox{Text=[["This is tragic. Please help me."]],Media=zmediasadpigmsg,}
end
if   ztaskCurlthePigstail.Complete == true then
Wherigo.MessageBox{Text=[["Strange things do happen! Either way, I'm a happy pig."]],Media=zmediahappypigmsg,}
end
end

function zcharacterChicken:OnTalk()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskCurlthePigstail.Complete == false and  not Player:Contains(zitemYellowEgg) then
Wherigo.MessageBox{Text=[["Oh! You're back. The pig's been crying for hours! Poor thing."

Hey! Don't go just yet.  You can take a look at my fantastic collection of eggs. I even have a yellow one. Go on! Look!"]],Media=zmediahenmsg,}
zitemYellowEgg:MoveTo(zoneTemperateForest)
zitemEgg:MoveTo(zoneTemperateForest)
zitemEgg2:MoveTo(zoneTemperateForest)
zitemYellowEgg.Visible = true
zitemEgg.Visible = true
zitemEgg2.Visible = true
end
if   ztaskCurlthePigstail.Complete == false and Player:Contains(zitemYellowEgg) then
Wherigo.MessageBox{Text=[["Hmm.. what will you do with the lemon anyway?"]],Media=zmediahenmsg,}
end
if   ztaskCurlthePigstail.Complete == true then
Wherigo.MessageBox{Text=[["The lemon curled pig's tail! Who knew? Anyway, hurry and find that pesky penguin."]],Media=zmediahenmsg,}
end
end

function zitemEgg:OnTake()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[["SQUAWK! Don't touch my egg!"]],Media=zmediahenmsg,}
end

function zitemEgg2:OnTake()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[["SQUAWK! I said LOOK! Not TAKE!"]],Media=zmediahenmsg,}
end

function zitemYellowEgg:OnTake()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.Dialog{{Text=[["Hey! That's my yellow egg! Wait a minute...is that a lemon?"]],Media=zmediahenmsg},{Text=[[New Inventory Item: Yellow Egg]],Media=zmedialemonmsg},}
zitemYellowEgg:MoveTo(Player)
zitemYellowEgg.Commands["Take"].Enabled = false
zitemYellowEgg.Commands["Give"].Enabled = true
end

function zitemYellowEgg:OnGive(target)
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   target == zcharacterPig then
zitemYellowEgg.Visible = false
Wherigo.Dialog{{Text=[["Eeeeeuuuuughhhh!!! That's really sour!!!! It really curls my tail. Lookie! I'll be the hit of the barn dance. Thank you so much."]],Media=zmediahappypigmsg},{Text=[["So, listen up. I'm going to tell you who did this to me. His name is Brian the penguin, and you need to catch him and his friend. Otherwise, you might spend all day trying to set this entire zoo straight."]],Media=zmediahappypigmsg},{Text=[[New Task: Find Brian the penguin.

]],Media=zmediabrianpenguin},{Text=[["I knew Brian the penguin was up to something. Let's go to the Tropical Rain Forest to learn more about what is going on."]],Media=zmediazookeepermsg},{Text=[[New Task: Go to the Tropical Rain Forest.

]],Media=zmediazonerainforest},}
ztaskCurlthePigstail.Complete = true
zcharacterPig.Media = zmediahappypig
zitemYellowEgg.Commands["Give"].Enabled = false
ztaskFindBrianthePenguin.Active = true
ztaskGototheTropicalRainForest.Active = true
zoneTropicalRainForest.Visible = true
zoneTropicalRainForest.Active = true
end
if   target == zcharacterChicken then
Wherigo.MessageBox{Text=[["Wow, I can't believe I've been nursing a lemon for weeks! So...no thanks."]],Media=zmediahenmsg,}
end
end

function zcharacterChicken:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[Chicken is proud of her eggs. They will hatch soon and little chicklets will roam the zoo!]],Media=zmediahenmsg,}
end

function zcharacterPig:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[A pig with a straight tail just does not seem right. ]],Media=zmediasadpigmsg,}
end

function zitemEgg:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[It's a good looking chicken egg. Warm too!]],Media=zmediaeggmsg,}
end

function zitemEgg2:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[A good looking egg- Chicken's pride and joy.]],Media=zmediaegg2msg,}
end

function zcharacterZookeeper:OnTalk()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskCurlthePigstail.Complete == false and ztaskFindBrianthePenguin.Active == false then
Wherigo.MessageBox{Text=[["The Pig's tail is straight. Who could have done it? Could it be one of the missing penguins?"]],Media=zmediazookeepermsg,}
end
if   ztaskCurlthePigstail.Complete == true and ztaskFindBrianthePenguin.Active == true then
Wherigo.MessageBox{Text=[["Brian...Brian..that little tricky penguin! Well, at least we set things straight here at Temperate Forest!"]],Media=zmediazookeepermsg,}
end
if   ztaskGototheTropicalRainForest.Complete == true and ztaskFindJaredthePenguin.Active == false then
Wherigo.MessageBox{Text=[["Alright. Here we are. Let's chat with these guys."]],Media=zmediazookeepermsg,}
end
if   ztaskGototheTropicalRainForest.Complete == true and ztaskFindJaredthePenguin.Active == true then
Wherigo.Dialog{{Text=[["Unfortunately, we can't find the penguins in this round. Let's go to the Penguin Pool anyway!"]],Media=zmediazookeepermsg},{Text=[[New Task: Go to the Penguin Pool.]],Media=zmediazonepenguinpool},}
ztaskGotothePenguinPool.Active = true
zonePenguinPool.Visible = true
zonePenguinPool.Active = true
end
if   ztaskGotothePenguinPool.Active == true and ztaskGotothePenguinPool.Complete == false then
Wherigo.MessageBox{Text=[["C'mon! we gotta get going. Aren't you curious?"]],Media=zmediazookeepermsg,}
end
if   ztaskGotothePenguinPool.Complete == true then
Wherigo.Dialog{{Text=[["I will see you soon!"]],Media=zmediazookeepermsg},}
end
end

function zitemYellowEgg:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[Eating one of these will curl anyone's toes. ]],Media=zmedialemonmsg,}
end

function zoneTemperateForest:OnExit()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskCurlthePigstail.Complete == true then
zcharacterZookeeper:MoveTo(zoneTropicalRainForest)
zcharacterColobus:MoveTo(zoneTropicalRainForest)
zcharacterGorilla:MoveTo(zoneTropicalRainForest)
end
end

function zoneTropicalRainForest:OnEnter()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
ztaskGototheTropicalRainForest.Complete = true
Wherigo.Dialog{{Text=[["The Tropical Rain Forest is home to our band of Gorillas, a Jaguar, and the Red Ruffed Lemur." 

Do you remember our last visit? Sounded like the gorilla was having some problems. Let's find out more!"]],Media=zmediazookeepermsg},{Text=[["This pink on my back just won't go away no matter what I do!"]],Media=zmediapinkbackgorillamsg},}
zcharacterGorilla.Visible = true
zcharacterZookeeper.Visible = true
ztaskGototheTropicalRainForest.Complete = true
end

function zcharacterGorilla:OnTalk()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskFindJaredthePenguin.Active == false and ztaskFixGorillashair.Active == false then
Wherigo.Dialog{{Text=[["That penguin offered to style my hair, but next time I'll be getting references. I can't trust anybody to touch my hair!"

Now my gorgeous silver hair is...this yucky pink."]],Media=zmediapinkbackgorillamsg},{Text=[["I normally have a shampoo, but I Iet Colobus borrow it a few weeks ago. Bad timing!"]],Media=zmediapinkbackgorillamsg},{Text=[[New Task: Fix Gorilla's hair]],Media=zmediapinkbackgorilla},}
zcharacterColobus.Visible = true
ztaskFixGorillashair.Active = true
elseif ztaskFixGorillashair.Active == true then
Wherigo.MessageBox{Text=[[*Sighs*  "Any other color would have been better!"]],Media=zmediapinkbackgorillamsg,}
end
if   ztaskFindJaredthePenguin.Active == true then
Wherigo.MessageBox{Text=[["Colobus needs to give me that shampoo right now! Either someone talk some sense into him or find that penguin! Quick!"]],Media=zmediapinkbackgorillamsg,}
end
end

function zcharacterColobus:OnTalk()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskFindJaredthePenguin.Active == false then
Wherigo.Dialog{{Text=[["You know, I kind of like Gorilla's new hairdo. Pink is one of my favorite colors. Oh no! don't be suspicious! I wasn't me! I promise. It's this penguin...he came in here and trashed my habitat too."]],Media=zmediacolobusmsg},{Text=[["...I'm sorry to say this, but I can't give you the Gorilla Shampoo unless you find that penguin for me. His name is Jared. Jared the Penguin."]],Media=zmediacolobusmsg},{Text=[[New Task: Find Jared the Penguin]],Media=zmediajaredpenguin},{Text=[["Hmm....I'm not sure if these animals can get help this time. We are going to have to move on. Talk to me for more information."]],Media=zmediazookeepermsg},}
ztaskFindJaredthePenguin.Active = true
elseif ztaskFindJaredthePenguin.Active == true then
Wherigo.MessageBox{Text=[["I promise! I'll give you the shampoo if you find the penguin. REALLY!"]],Media=zmediacolobusmsg,}
end
end

function zcharacterGorilla:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[Adult male gorillas eat about 70 pounds of food per day. That's about 280 Quarter-Pounder cheeseburgeres from McDonalds.]],Media=zmediapinkbackgorillamsg,}
end

function zcharacterColobus:OnExamine()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
Wherigo.MessageBox{Text=[[Colobus monkeys were once thought to be abnormal because they have no thumb. Instead, these creatures have only a small stub where a tumb would usually be. This is actually an adaptation which allows colobus monkeys to travel easily along the tops of branches.]],Media=zmediacolobusmsg,}
end

function zoneTropicalRainForest:OnExit()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
if   ztaskFindJaredthePenguin.Active == true and ztaskFixGorillashair.Active == true and ztaskFixGorillashair.Active == true then
zcharacterZookeeper:MoveTo(zonePenguinPool)
end
end

function zonePenguinPool:OnEnter()
-- #GroupDescription=Script --
-- #Comment=Script Comment --
ztaskGotothePenguinPool.Complete = true
Wherigo.Dialog{{Text=[["Here we are again, at the penguin pool. The two penguins are still missing, but at least we now know who they are!"]],Media=zmediazookeepermsg},{Text=[["Next level, we're going to complete the rest of the tasks and find all the penguins- well..if you're up for it!"]],Media=zmediazookeepermsg},{Text=[[This sample cartridge has been brought to you by J2B2!]],Media=zmediapenguingroupmsg},}
cartZooventureLevelTwo.Complete = true
end
------End Builder Generated functions, Do not Edit, this will be overwritten------
-------------------------------------------------------------------------------
------Builder Generated callbacks, Do not Edit, this will be overwritten------
-------------------------------------------------------------------------------
--#LASTCALLBACKKEY=0#--
------End Builder Generated callbacks, Do not Edit, this will be overwritten------
-- #Author Functions Go Here# --
-- #End Author Functions# --
-- Nothing after this line --
return cartZooventureLevelTwo

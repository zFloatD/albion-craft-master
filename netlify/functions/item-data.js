exports.handler = async function(event) {
  const id = (event.queryStringParameters && event.queryStringParameters.id || "").trim();
  if (!id) return { statusCode: 400, headers: {"content-type":"application/json; charset=utf-8"}, body: JSON.stringify({error:"missing id"}) };

  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), 6500);
  try {
    const url = "https://gameinfo.albiononline.com/api/gameinfo/items/" + encodeURIComponent(id) + "/data";
    const r = await fetch(url, {signal: controller.signal, headers: {"accept":"application/json"}});
    if (!r.ok) throw new Error("Gameinfo HTTP " + r.status);
    const data = await r.json();
    return {
      statusCode: 200,
      headers: {
        "content-type":"application/json; charset=utf-8",
        "cache-control":"public, max-age=3600"
      },
      body: JSON.stringify(data)
    };
  } catch (e) {
    return {
      statusCode: 502,
      headers: {"content-type":"application/json; charset=utf-8", "cache-control":"no-store"},
      body: JSON.stringify({error:"item data unavailable", detail:e.name === "AbortError" ? "timeout" : e.message})
    };
  } finally {
    clearTimeout(timer);
  }
};
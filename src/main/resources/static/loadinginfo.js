async function spinnerLoading(id) {
    $(':button').prop('disabled', true);
    let finished = false;
    let count = 0;
    console.log("Init");
    while (!finished) {
        try {
            const res = await getData();
            console.log(res);
            document.getElementById(id).innerHTML = '<span>Generiere PDFs: [' + res["progress"] + '/' + res["size"] + ']</span>';
            if (res["progress"] === 0) {
                count++;
            }
            finished = res["finished"];
            if (count >= 4) {
                finished = true;
            }

        } catch (err) {
            console.log(err);
        }
        await sleep(250);
    }
    $(':button').prop('disabled', false);
    document.getElementById(id).innerHTML = 'Download';

}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function getData() {
    return $.ajax({
        url: "progress",
        type: 'GET'
    });
}

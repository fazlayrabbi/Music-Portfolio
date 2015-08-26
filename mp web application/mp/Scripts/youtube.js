var pid;
var videoTitle = new Array;
var videoId = new Array;
var thumbnail = new Array;

function changeframe(b) {
    var output = '//www.youtube.com/embed/' + videoId[b];
    $('#youtubeframe').attr("src", output);
}


function getYoutubeData(username) {
    
       $.get(
            "https://www.googleapis.com/youtube/v3/channels",
            { part: 'contentDetails', forUsername: username, key: 'AIzaSyDVlOqQqTU2Q5p7fhCUwPc_rYt1nEnsEB8' },
            function (data) {
                $.each(data.items, function (i, item) {
                    console.log(item);
                    pid = item.contentDetails.relatedPlaylists.uploads;
                    getvids(pid);
                })
            }
        );
            }
        function getvids(pid) {
            $.get(
            "https://www.googleapis.com/youtube/v3/playlistItems",
            { part: 'snippet', maxResults: 20, playlistId: pid, key: 'AIzaSyDVlOqQqTU2Q5p7fhCUwPc_rYt1nEnsEB8' },
            function (data) {

                $.each(data.items, function (i, item) {
                    console.log(item.snippet.thumbnails.default.url);
                    videoTitle.push(item.snippet.title);
                    videoId.push(item.snippet.resourceId.videoId);
                    thumbnail.push(item.snippet.thumbnails.default);
                    if (i == 0) {
                        var output = '//www.youtube.com/embed/' + videoId[0];
                        $('#youtubeframe').attr("src", output);

                    }
                    var divid = '#thumbnail' + i;
                    var aid = '#popoverData' + i;
                    console.log(divid);
                    $(aid).attr("data-original-title", item.snippet.title);
                    $(divid).attr("src", item.snippet.thumbnails.default.url);
                })
                $('#popoverData1').popover({ html: true, container: 'body' }); $('#popoverData2').popover({ html: true, container: 'body' });
        $('#popoverData3').popover({ html: true, container: 'body' }); $('#popoverData5').popover({ html: true, container: 'body' });
        $('#popoverData6').popover({ html: true, container: 'body' }); $('#popoverData7').popover({ html: true, container: 'body' });
        $('#popoverData8').popover({ html: true, container: 'body' }); $('#popoverData9').popover({ html: true, container: 'body' });
        $('#popoverData4').popover({ html: true, container: 'body' }); $('#popoverData0').popover({ html: true, container: 'body' });
        $('#popoverData10').popover({ html: true, container: 'body' }); $('#popoverData11').popover({ html: true, container: 'body' });
        $('#popoverData12').popover({ html: true, container: 'body' }); $('#popoverData13').popover({ html: true, container: 'body' });
        $('#popoverData14').popover({ html: true, container: 'body' }); $('#popoverData15').popover({ html: true, container: 'body' });
        $('#popoverData16').popover({ html: true, container: 'body' }); $('#popoverData17').popover({ html: true, container: 'body' });
        $('#popoverData18').popover({ html: true, container: 'body' }); $('#popoverData19').popover({ html: true, container: 'body' });

            }
             );
        }
